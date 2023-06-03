package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoOrderShopee;
import com.vvlanding.repo.RepoChannelShopee;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.shopee.chat.*;
import com.vvlanding.shopee.service.shopee.ChatService;
import com.vvlanding.shopee.service.shopee.ShopAuthServices;
import com.vvlanding.shopee.service.shopee.ShopeeService;
import com.vvlanding.table.ChannelShopee;
import com.vvlanding.table.ShopInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("api/shopee/chat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ShopeeChatAPI {

    private long partnerIdV2 = Iconstants.partner_id_customer_v2;

    private String partnerKeyV2 = Iconstants.test_key_customer_v2;

    private String domain = Iconstants.domain;

    @Autowired
    ShopeeService shopeeService;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    ChatService chatService;

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    RepoChannelShopee repoChannelShopee;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    SimpMessagingTemplate templateSocket;

    @RequestMapping(value = "/get/shop/{shopID}",method = RequestMethod.GET)
    public ResponseEntity<?> getShopAppCustomer(@PathVariable long shopID, @RequestParam String code, @RequestParam(name = "shop_id") long shopeeShopId){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopID);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeShopId);
        if (channelShopee.isPresent() && channelShopee.get().getShop().getId() != shopID){
            response.put("message","tài khoản shopee đã đăng ký ở landing khác");
            response.put("success",false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        shopeeService.getAccess_token(shopInfo.get(),code,shopeeShopId,partnerKeyV2,partnerIdV2,3L);
        String access_token = shopeeService.getToken(shopeeShopId, partnerKeyV2, partnerIdV2, 3L);
        if (access_token.equals("invalid")){
            response.put("message","Token của shopee đã hết hạn.Hãy kết nối lại shopee");
            response.put("success",false);
            return ResponseEntity.badRequest().body(response);
        }
        ResponseEntity responseEntity = shopeeService.getShop(shopInfo.get(),shopeeShopId,partnerKeyV2,partnerIdV2,3L);
        List<ResponseDTO> res = chatService.getListChat(shopeeShopId,"latest",0L,access_token);
        try {
            for (ResponseDTO r:
                    res) {
                for (Conversations c:
                        r.getResponse().getConversations()) {
                    chatService.getMessage(shopeeShopId,partnerKeyV2,partnerIdV2,c.getConversation_id(),shopID,access_token);
                }

            }
            return responseEntity;
        }catch (Exception e){
            response.put("success",false);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
    //get url kết nối app customer mobile
    @GetMapping(value = "/get/url/v2")
    public ResponseEntity<?> getAuth() {
        String shopeeRedirectUrl = "https://mobileapi.vipage.vn/";
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",ShopAuthServices.authShopV2(partnerIdV2,shopeeRedirectUrl,partnerKeyV2));
        return new ResponseEntity<>(statusMessage,HttpStatus.OK);
    }

    //get url kết nối app customer web
    @GetMapping(value = "/get/url/v2/web")
    public ResponseEntity<?> getAuthweb() {
        String shopeeRedirectUrl = "https://chat.vipage.vn/chatShopee";
//        String shopeeRedirectUrl = "http://localhost:6969/";
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",ShopAuthServices.authShopV2(partnerIdV2,shopeeRedirectUrl,partnerKeyV2));
        return new ResponseEntity<>(statusMessage,HttpStatus.OK);
    }

    @RequestMapping(value = "/get/access/token/{shopId}",method = RequestMethod.GET)
    public ResponseEntity<?> getTokenAppCustomer(@PathVariable long shopId ,@RequestParam long shop_id,@RequestParam String code){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",shopeeService.getAccess_token(shopInfo.get(),code,shop_id,partnerKeyV2,partnerIdV2,3L));
        return new ResponseEntity<>(statusMessage,HttpStatus.OK);
    }

    @RequestMapping(value = "/get/list/chat/{shopId}" ,method = RequestMethod.GET)
    public ResponseEntity<?> getListChat(@PathVariable Long shopId, @RequestParam Long shopeeId){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            response.put("success",false);
            response.put("data",null);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        String access_token = shopeeService.getToken(shopeeId, partnerKeyV2, partnerIdV2, 3L);
        if (access_token.equals("invalid")){
            response.put("message","Token của shopee đã hết hạn.Hãy kết nối lại shopee");
            response.put("success",false);
            response.put("data",null);
            return ResponseEntity.badRequest().body(response);
        }
        List<ResponseDTO> res = chatService.getListChat(shopeeId,"latest",0L,access_token);
        try {
            for (ResponseDTO r:
                    res) {
                for (Conversations c:
                        r.getResponse().getConversations()) {
                    chatService.getMessage(shopeeId,partnerKeyV2,partnerIdV2,c.getConversation_id(),shopId,access_token);
                }
                response.put("success",true);
                response.put("message","");
                response.put("data",res);
                return new ResponseEntity<>(response,HttpStatus.OK);
            }
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
            response.put("data",null);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        response.put("success",true);
        response.put("data",null);
        response.put("message","");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    //lấy nội dung chat
    @RequestMapping(value = "/get/message/{shopId}",method = RequestMethod.GET)
    public ResponseEntity<?> getMessage(@PathVariable Long shopId, @RequestParam Long shopeeId,@RequestParam String conversation_id){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            response.put("success",false);
            response.put("data",null);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        String access_token = shopeeService.getToken(shopeeId, partnerKeyV2, partnerIdV2, 3L);
        if (access_token.equals("invalid")){
            response.put("message","Token của shopee đã hết hạn.Hãy kết nối lại shopee");
            response.put("success",false);
            return ResponseEntity.badRequest().body(response);
        }
        response.put("success",true);
        response.put("message","");
        response.put("data",chatService.getMessage(shopeeId,partnerKeyV2,partnerIdV2,conversation_id, shopId,access_token));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // gửi tin nhắn
    @RequestMapping(value = "/get/send/message/{shopId}",method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@PathVariable Long shopId, @RequestParam String conversation_id, @RequestParam long shopeeId,@RequestBody SendValue sendValue){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            response.put("success",true);
            response.put("data",null);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        String access_token = shopeeService.getToken(shopeeId, partnerKeyV2, partnerIdV2, 3L);
        if (access_token.equals("invalid")){
            response.put("message","Token của shopee đã hết hạn.Hãy kết nối lại shopee");
            response.put("success",false);
            response.put("data",null);
            return ResponseEntity.badRequest().body(response);
        }
        sendValue.setConversation_id(conversation_id);
        sendValue.setShopeeId(shopeeId);
        sendValue.setShopId(shopId);
        sendValue.setPartnerIdV2(partnerIdV2);
        sendValue.setPartnerKeyV2(partnerKeyV2);
        chatService.sendMessageQueue(sendValue);
        response.put("message","");
        response.put("success",true);
        response.put("data",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // gửi tin nhắn ảnh
    @RequestMapping(value = "/get/send/message/image/{shopId}",method = RequestMethod.POST)
    public ResponseEntity<?> sendMessageImage(@PathVariable Long shopId, @RequestParam String conversation_id, @RequestParam long shopeeId,MultipartFile file){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            response.put("success",true);
            response.put("data",null);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        String access_token = shopeeService.getToken(shopeeId, partnerKeyV2, partnerIdV2, 3L);
        if (access_token.equals("invalid")){
            response.put("message","Token của shopee đã hết hạn.Hãy kết nối lại shopee");
            response.put("success",false);
            response.put("data",null);
            return ResponseEntity.badRequest().body(response);
        }
        String url = chatService.sendImage(shopeeId,partnerKeyV2,partnerIdV2,file,shopId,conversation_id);
        QueueSendImage sendImage = new QueueSendImage();
        sendImage.setConversation_id(conversation_id);
        sendImage.setShopeeId(shopeeId);
        sendImage.setShopId(shopId);
        sendImage.setPartnerID(partnerIdV2);
        sendImage.setPartnerKey(partnerKeyV2);
        sendImage.setFile(url);
//        template.convertAndSend(MQConfig.EXCHANGE_IMAGE,
//                MQConfig.ROUTING_KEY_IMAGE, sendImage);
        chatService.sendMessageImageQueue(sendImage);
        response.put("message","");
        response.put("success",true);
        response.put("data",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/pin/{shopId}",method = RequestMethod.POST)
    public ResponseEntity<?> pinChat(@PathVariable Long shopId,@RequestParam long shopeeId, @RequestParam Long conversation_id,@RequestParam boolean pin){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        String access_token = shopeeService.getToken(shopeeId, partnerKeyV2, partnerIdV2, 3L);
        if (access_token.equals("invalid")){
            response.put("message","Token của shopee đã hết hạn.Hãy kết nối lại shopee");
            response.put("success",false);
            response.put("data",null);
            return ResponseEntity.badRequest().body(response);
        }
        if (pin == true){
            return chatService.pinConversation(shopeeId,partnerKeyV2,partnerIdV2,conversation_id);
        }else {
            return chatService.unpinConversation(shopeeId,partnerKeyV2,partnerIdV2,conversation_id);
        }
    }
    @RequestMapping(value = "/get/chatdetail/{shopId}",method = RequestMethod.GET)
    public ResponseEntity<?> getAllChatDetail(@PathVariable Long shopId){
        return new ResponseEntity<>(chatService.getAllChatDetail(shopId),HttpStatus.OK);
    }

    @RequestMapping(value = "/get/message/data",method = RequestMethod.GET)
    public ResponseEntity getAllMessage(@RequestParam String conversationId, @RequestParam Long shopeeId){
        return new ResponseEntity(chatService.getMessageData(conversationId, shopeeId),HttpStatus.OK);
    }

    @RequestMapping(value = "/get/chatdetail/page/{shopId}",method = RequestMethod.GET)
    public ResponseEntity<?> getAllChat(@PathVariable Long shopId, Pageable pageable){
        return new ResponseEntity<>(chatService.getAllChatDetailPageable(shopId,pageable),HttpStatus.OK);
    }

    @RequestMapping(value = "/get/message",method = RequestMethod.GET)
    public ResponseEntity<?> getAllMessagePageable(@RequestParam String conversationId ,@RequestParam Long shopeeId , Pageable pageable){
        return new ResponseEntity<>(chatService.getMessageDataPageable(conversationId,shopeeId,pageable),HttpStatus.OK);
    }
    @RequestMapping(value = "/get/name",method = RequestMethod.GET)
    public ResponseEntity<?> getToName(@RequestParam Long shopInfoId,@RequestParam String toName){
        return chatService.getToName(toName,shopInfoId);
    }

    @RequestMapping(value = "/get/total",method = RequestMethod.GET)
    public ResponseEntity<?> getTotalShopee(@RequestParam Long shopInfoId){
        return chatService.getTotal(shopInfoId);
    }

//    @RequestMapping(value = "/get/order",method = RequestMethod.GET)
//    public ResponseEntity getOrder(@RequestParam Long shopeeId){
//        return chatService.getListOrder(shopeeId);
//    }

    @RequestMapping(value = "/get/product/{shopId}",method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@RequestParam Long shopeeId,@PathVariable Long shopId){
        return chatService.getListProduct(shopeeId,shopId);
    }

    @RequestMapping(value = "/onview",method = RequestMethod.GET)
    public ResponseEntity<?> getOnView(@RequestParam Long id){
        return chatService.onView(id);
    }

    @RequestMapping(value = "/get/content",method = RequestMethod.GET)
    public ResponseEntity<?> getContent(@RequestParam String conversationId,String content){
        return chatService.findByContent(conversationId,content);
    }
//    @RequestMapping(value = "/get/order",method = RequestMethod.GET)
//    private ResponseEntity<?> getOrder(@RequestParam Long shopeeId,String toId){
//        return chatService.getByOrder(shopeeId,toId);
//    }

    @RequestMapping(value = "/get/order", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getBillOfShop( String query, @RequestParam Long shopeeId,String toId, Pageable pageable) {
        Map<String, Object> repose = new HashMap<>();
            try {
                int start;
                int end;
                List<DtoOrderShopee> listDtoBill;
                Resp resp = new Resp();
                Pageable pageable1;
                int total = 0;
                boolean b = query == null || query.equals(null);
                if (pageable.getPageNumber() == 0 || pageable.getPageNumber() == 1) {
                    if (b) {
                        start = (int) PageRequest.of(0, 5).getOffset();
                        listDtoBill = chatService.getBillOfShop(shopeeId, toId);
                        total = listDtoBill.size();
                        end = (start + 5) > listDtoBill.size() ? listDtoBill.size() : 5;
                        resp.setMetaRepo(1, 5, listDtoBill.size());
                        listDtoBill = listDtoBill.subList(start, end);
                    } else {
                        listDtoBill = chatService.getBillOfShopByQuery(shopeeId, toId, query);
                        total = listDtoBill.size();
                        start = (int) PageRequest.of(0, 5).getOffset();
                        end = (start + 5) > listDtoBill.size() ? listDtoBill.size() : 5;
                        resp.setMetaRepo(1, 5, listDtoBill.size());
                        listDtoBill = listDtoBill.subList(start, end);
                    }
                    resp.setMetaRepo(1, 5, total);
                    repose.put("data", listDtoBill);
                } else {
                    pageable1 = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
                    if (b) {
                        listDtoBill = chatService.getBillOfShopPage(shopeeId, toId, pageable1);
                        total = chatService.getBillOfShop(shopeeId, toId).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoBill);
                    } else {
                        listDtoBill = chatService.getBillOfShopByQueryPage(shopeeId, toId, query, pageable1);
                        total = chatService.getBillOfShopByQuery(shopeeId, toId, query).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoBill);
                    }
                }
                repose.put("meta", resp.getMeta());
                repose.put("success", true);
                repose.put("message", "Ok");
                return new ResponseEntity<>(repose, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                repose.put("success", false);
                repose.put("error", e.getMessage());
                return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);

        }
    }
}
