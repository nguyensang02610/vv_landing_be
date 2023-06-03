package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoChannelShopee;
import com.vvlanding.shopee.order.order.*;
import com.vvlanding.shopee.service.shopee.ChatService;
import com.vvlanding.shopee.service.shopee.ItemShopeeService;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.shopee.service.shopee.ShopeeService;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("api/shopee")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ShopeeAPI {

    @Autowired
    ShopeeService shopeeService;

    @Autowired
    ItemShopeeService itemShopeeService;

    @Autowired
    OrderServices orderServices;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    ChatService chatService;

    @Autowired
    SerShopInfo serShopInfo;

    @RequestMapping(value = "/get/channel/shopee/{shopId}",method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getShopfromShopee(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, String query, Pageable pageable) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            try {
                int start;
                int end;
                List<DtoChannelShopee> listDtoChannelShopee;
                Resp resp = new Resp();
                Pageable pageable1;
                int total = 0;
                boolean b = query == null || query.equals(null);
                if (pageable.getPageNumber() == 0 || pageable.getPageNumber() == 1) {
                    if (b) {
                        start = (int) PageRequest.of(0, 10).getOffset();
                        listDtoChannelShopee = shopeeService.getShopeeOfShop(shopInfo);
                        total = listDtoChannelShopee.size();
                        end = (start + 10) > listDtoChannelShopee.size() ? listDtoChannelShopee.size() : 10;
                        resp.setMetaRepo(1, 10, listDtoChannelShopee.size());
                        listDtoChannelShopee = listDtoChannelShopee.subList(start, end);
                    } else {
                        listDtoChannelShopee = shopeeService.getShopeeOfShopByQuery(shopInfo, query, query);
                        total = listDtoChannelShopee.size();
                        start = (int) PageRequest.of(0, 10).getOffset();
                        end = (start + 10) > listDtoChannelShopee.size() ? listDtoChannelShopee.size() : 10;
                        resp.setMetaRepo(1, 10, listDtoChannelShopee.size());
                        listDtoChannelShopee = listDtoChannelShopee.subList(start, end);
                    }
                    resp.setMetaRepo(1, 10, total);
                    repose.put("data", listDtoChannelShopee);
                } else {
                    pageable1 = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
                    if (b) {
                        listDtoChannelShopee = shopeeService.getShopeeOfShopPage(shopInfo, pageable1);
                        total = shopeeService.getShopeeOfShop(shopInfo).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoChannelShopee);
                    } else {
                        listDtoChannelShopee = shopeeService.getShopeeOfShopByQueryPage(shopInfo, query, query, pageable1);
                        total = shopeeService.getShopeeOfShopByQuery(shopInfo, query, query).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoChannelShopee);
                    }
                }
                repose.put("meta", resp.getMeta());
                repose.put("success", true);
                repose.put("message", "Ok");
                return new ResponseEntity<>(repose, HttpStatus.OK);
            } catch (Exception e) {
                repose.put("success", false);
                repose.put("error", e.getMessage());
                return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
            }
        }
    }

//    @RequestMapping(value = "/get/{shopId}/shopee",method = RequestMethod.GET)
//    public ResponseEntity getShopee(@CurrentUser UserPrincipal currentUser,@PathVariable long shopId, @RequestParam(name = "shop_id") long shopeeShopId) throws JsonProcessingException {
//        long timestamp = Common.getCurrentTime();
//        Map<String,Object> response = new HashMap<>();
//        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);
//        if (!shopInfo.isPresent()){
//            response.put("message","không tìm thấy thông tin shop");
//            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
//        }
//
//        ShopInforRequestDTO inforRequestDTO = new ShopInforRequestDTO(shopeeShopId, partnerID, "",timestamp);
//
//        if (shopInfo.isPresent()) {
//            try {
//                shopeeService.getShopInfo(partnerKey,inforRequestDTO,shopeeShopId,shopId);
//
//                itemShopeeService.fetchProduct(currentUser,shopeeShopId,shopId);
//
//                List<Orders> fetchOrders = orderServices.fetchOrders(shopInfo.get(), shopeeShopId);
//
//                try {
//                    response.put("data",orderServices.fetchListOrderDetail(shopInfo.get(),shopeeShopId, fetchOrders));
//                    return new ResponseEntity(response,HttpStatus.OK);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success","ok");
//        return new ResponseEntity(statusMessage,HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/get/{shopID}/shop",method = RequestMethod.POST)
//    public ResponseEntity loginSuccess(@PathVariable long shopID, @RequestParam(name = "shop_id") long shopeeShopId) throws JsonProcessingException {
//        long timestamp = Common.getCurrentTime();
//        ShopInforRequestDTO inforRequestDTO = new ShopInforRequestDTO(shopeeShopId, partnerID, timestamp);
//        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",shopeeService.getShopInfo(partnerKey,inforRequestDTO,shopeeShopId,shopID));
//        return new ResponseEntity(statusMessage,HttpStatus.OK);
//    }



//    @GetMapping(value = "/get/url")
//    @CrossOrigin(origins = "*", maxAge = 3600)
//    public ResponseEntity manageShopee() {
//        String shopeeRedirectUrl = domain + "/shopee";
//        ShopAuthReqDTO shopAuthReqDTO = new ShopAuthReqDTO(partnerID, partnerKey, shopeeRedirectUrl);
//        ShopAuthResDTO authShop = ShopAuthServices.authShop(shopAuthReqDTO);
//        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",authShop.getAuthUrl());
//        return new ResponseEntity(statusMessage,HttpStatus.OK);
//    }

//    @RequestMapping(value = "/get/{shopId}/list/item",method = RequestMethod.GET)
//    public ResponseEntity getListItem(@CurrentUser UserPrincipal currentUser,@PathVariable long shopId, @RequestParam(name = "shop_id") List<Long> shopeeShopId) throws JsonProcessingException {
//        for (Long l:shopeeShopId) {
//            itemShopeeService.fetchProduct(currentUser,l,shopId);
//        }
//        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success","ok");
//        return new ResponseEntity(statusMessage,HttpStatus.OK);
//    }



//    @RequestMapping(value = "/get/{shopId}/list/order",method = RequestMethod.GET)
//    public ResponseEntity getListOrder(@PathVariable long shopId , @RequestParam List<Long> shopeeShopId){
//        Map<String,Object> response = new HashMap<>();
//        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);
//        if (!shopInfo.isPresent()){
//            response.put("message","không tìm thấy thông tin shop");
//            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
//        }
//        if (shopInfo.isPresent()) {
//            try {
//                for (Long l:shopeeShopId) {
//
//                    List<Orders> fetchOrders = orderServices.fetchOrders(shopInfo.get(), l);
//
//                    try {
//                        response.put("data",orderServices.fetchListOrderDetail(shopInfo.get(),l, fetchOrders));
//                        return new ResponseEntity(response,HttpStatus.OK);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
    @RequestMapping(value = "/get/access/token/merchant/{shopId}",method = RequestMethod.GET)
    public ResponseEntity getTokenMerchant(@PathVariable long shopId ,@RequestParam long main_account_id,@RequestParam String code){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",shopeeService.getAccess_token_merchant(shopInfo.get(),code,main_account_id));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }


    @RequestMapping(value = "/cancel/order/{shopId}",method = RequestMethod.POST)
    public ResponseEntity cancelOrderV1(@PathVariable Long shopId,@RequestParam Long billId){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }
        return orderServices.cancelOrder(billId);
    }

    @RequestMapping(value = "get/shopee/{shopId}",method = RequestMethod.GET)
    public ResponseEntity getShopee(@PathVariable long shopId){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(shopeeService.getChannelShopee(shopId),HttpStatus.OK);
    }
}
