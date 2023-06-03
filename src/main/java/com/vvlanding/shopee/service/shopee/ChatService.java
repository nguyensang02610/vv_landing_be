package com.vvlanding.shopee.service.shopee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.dto.*;
import com.vvlanding.repo.*;
import com.vvlanding.shopee.Common;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.shopee.chat.*;
import com.vvlanding.shopee.item.v2.DtoProductShopee;
import com.vvlanding.table.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ShopeeService shopeeService;

    @Autowired
    private RepoChannelShopee repoChannelShopee;

    @Autowired
    private RepoChatShopee repoChatShopee;

    @Autowired
    private RepoChatDetail repoChatDetail;

    @Autowired
    private RepoProductShopee repoProductShopee;

    @Autowired
    private RepoOrderChannel repoOrderChannel;

    @Autowired
    SimpMessagingTemplate template;

    public List<ResponseDTO> getListChat(Long shopeeId, String direction, Long next_timestamp_nano,String access_token) {
        List<ResponseDTO> responseDTOList = new ArrayList<>();
        ResponseDTO responseDTO = getListConversation(shopeeId, direction, next_timestamp_nano,access_token);
        responseDTOList.add(responseDTO);
        boolean more = responseDTO.getResponse().getPage_result().isMore();
        if (more == true) {
            int a = 0;
            Long time = responseDTO.getResponse().getPage_result().getNext_cursor().getNext_message_time_nano();
            while (more == true && a < 5) {
                String str = "older";
                a++;
                ResponseDTO res = getListConversation(shopeeId, str, time,access_token);
                responseDTOList.add(res);
                more = res.getResponse().getPage_result().isMore();
                if (more == true) {
                    time = res.getResponse().getPage_result().getNext_cursor().getNext_message_time_nano();
                }
            }
        }
        return responseDTOList;
    }

    public ResponseDTO getListConversation(Long shopeeId, String direction, Long next_timestamp_nano,String access_token) {
        Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeId);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/sellerchat/get_conversation_list";
        String url = "?direction=" + direction + "&type=all&page_size=60";
        if (next_timestamp_nano != 0) {
            url = url + "&next_timestamp_nano" + next_timestamp_nano;
        }
        int cnt = 0;
        ResponseDTO conversations = null;
        while (conversations == null && cnt < 5) {
            cnt++;
            try {
                conversations = Common.callAPIV2(url, host, path, access_token, String.valueOf(shopeeId), Iconstants.test_key_customer_v2, String.valueOf(Iconstants.partner_id_customer_v2), null, ResponseDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (conversations != null && conversations.getResponse().getConversations() != null) {
                List<Conversations> conversationsList = conversations.getResponse().getConversations();
                for (Conversations c : conversationsList) {
                    Optional<ChatDetail> chat = repoChatDetail.findByConversationId(c.getConversation_id());
                    if (chat.isPresent()) {
                        ChatDetail chatDetail = chat.get();
                        if (c.getLatest_message_type().equals("text") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(c.getLatest_message_content().getText());
                        } else if (c.getLatest_message_type().equals("sticker") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(c.getLatest_message_content().getSticker_id());
                        } else if (c.getLatest_message_type().equals("image") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(c.getLatest_message_content().getUrl());
                        } else if (c.getLatest_message_type().equals("order") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(c.getLatest_message_content().getOrder_sn());
                        } else if (c.getLatest_message_type().equals("item") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(String.valueOf(c.getLatest_message_content().getItem_id()));
                        }
                        long time = c.getLast_message_timestamp() / 1000000;
                        Date date = new Date(time);
                        chatDetail.setLastMessageTimestamp(date);
                        chatDetail.setPinned(c.isPinned());
                        repoChatDetail.save(chatDetail);
                    } else {
                        ChatDetail chatDetail = new ChatDetail();
                        chatDetail.setConversationId(c.getConversation_id());
                        if (c.getLatest_message_type().equals("text") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(c.getLatest_message_content().getText());
                        } else if (c.getLatest_message_type().equals("sticker") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(c.getLatest_message_content().getSticker_id());
                        } else if (c.getLatest_message_type().equals("image") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(c.getLatest_message_content().getUrl());
                        } else if (c.getLatest_message_type().equals("order") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(c.getLatest_message_content().getOrder_sn());
                        } else if (c.getLatest_message_type().equals("item") && c.getLatest_message_content() != null) {
                            chatDetail.setContent(String.valueOf(c.getLatest_message_content().getItem_id()));
                        }
                        long time = c.getLast_message_timestamp() / 1000000;
                        Date date = new Date(time);
                        chatDetail.setLastMessageTimestamp(date);
                        chatDetail.setShopId(c.getShop_id());
                        chatDetail.setMessage_type(c.getLatest_message_type());
                        chatDetail.setPinned(c.isPinned());
                        chatDetail.setTo_avatar(c.getTo_avatar());
                        chatDetail.setToName(c.getTo_name());
                        chatDetail.setToId(c.getTo_id());
                        chatDetail.setShopInfoId(channelShopee.get().getShop().getId());
                        repoChatDetail.save(chatDetail);
                    }
                }
            }
        }
        return conversations;
    }

    public ResponseEntity getMessage(Long shopeeId, String partnerKey, Long partnerID, String conversation_id, Long shopId,String access_token) {

        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/sellerchat/get_message";
        String url = "?offset=0&page_size=60&conversation_id=" + conversation_id;
        int cnt = 0;
        ResponseDTO response = null;
        while (response == null && cnt < 5) {
            cnt++;
            try {
                response = Common.callAPIV2(url, host, path, access_token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), null, ResponseDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopIdAndShopId(shopeeId, shopId);
            Optional<ChatDetail> chatDetail = repoChatDetail.findByConversationIdAndShopInfoId(conversation_id, shopId);
            if (channelShopee.isPresent() && response.getMessage() != null && chatDetail.isPresent()) {
                List<MessageResponse> messageResponses = response.getResponse().getMessages();
                List<ChatShopee> chatShopees = new ArrayList<>();
                for (MessageResponse m :
                        messageResponses) {
                    Optional<ChatShopee> chatShopee = repoChatShopee.findByMessageId(m.getMessage_id());
                    if (!chatShopee.isPresent()) {
                        ChatShopee chat = new ChatShopee();
                        if (m.getMessage_type().contains("text") && m.getContent() != null) {
                            chat.setContent(m.getContent().getText());
                        } else if (m.getMessage_type().contains("sticker") && m.getContent() != null) {
                            chat.setContent(m.getContent().getSticker_id());
                        } else if (m.getMessage_type().contains("image") && m.getContent() != null) {
                            chat.setContent(m.getContent().getUrl());
                        } else if (m.getMessage_type().contains("order") && m.getContent() != null) {
                            chat.setContent(m.getContent().getOrder_sn());
                        } else if (m.getMessage_type().contains("item") && m.getContent() != null) {
                            chat.setContent(String.valueOf(m.getContent().getItem_id()));
                        }
                        if (chatDetail.get().getToId() != m.getTo_id()) {
                            chat.setSender(0);
                        } else {
                            chat.setSender(1);
                        }
                        long time = m.getCreated_timestamp() * 1000;
                        Date date = new Date(time);
                        chat.setCreateDate(date);
                        chat.setMessageType(m.getMessage_type());
                        chat.setChannelShopee(channelShopee.get());
                        chat.setConversationId(m.getConversation_id());
                        chat.setFromShopId(m.getFrom_shop_id());
                        chat.setFromId(m.getFrom_id());
                        chat.setToShopId(m.getTo_shop_id());
                        chat.setToId(m.getTo_id());
                        chat.setChatDetail(chatDetail.get());
                        chat.setMessageId(m.getMessage_id());
                        chatShopees.add(chat);
                    }
                }
                repoChatShopee.saveAll(chatShopees);
            }
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

//    public ResponseEntity<?> getOneConversation(Long conversation_id, Long shopeeId, Long shopId) {
//        String access_token = shopeeService.getToken(shopeeId, Iconstants.test_key_customer_v2, Iconstants.partner_id_customer_v2, 3L);
//        String host = "https://partner.shopeemobile.com";
//        String path = "/api/v2/sellerchat/get_one_conversation";
//        String url = "?conversation_id=" + conversation_id;
//        ResponseConversation res = null;
//        int cnt = 0;
//        while (res == null && cnt < 5) {
//            cnt++;
//            try {
//                res = Common.callAPIV2(url, host, path, access_token, String.valueOf(shopeeId), Iconstants.test_key_customer_v2, String.valueOf(Iconstants.partner_id_customer_v2), null, ResponseConversation.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//                res = null;
//            }
//        }
//        Conversations c = res.getResponse();
//        ChatDetail chatDetail = new ChatDetail();
//        chatDetail.setConversationId(c.getConversation_id());
//        if (c.getLatest_message_type().equals("text")) {
//            chatDetail.setContent(c.getLatest_message_content().getText());
//        } else if (c.getLatest_message_type().equals("sticker")) {
//            chatDetail.setContent(c.getLatest_message_content().getSticker_id());
//        } else if (c.getLatest_message_type().equals("image")) {
//            chatDetail.setContent(c.getLatest_message_content().getUrl());
//        } else if (c.getLatest_message_type().equals("order")) {
//            chatDetail.setContent(c.getLatest_message_content().getOrder_sn());
//        } else if (c.getLatest_message_type().equals("item")) {
//            chatDetail.setContent(String.valueOf(c.getLatest_message_content().getItem_id()));
//        }
//        chatDetail.setShopId(c.getShop_id());
//        chatDetail.setMessage_type(c.getLatest_message_type());
//        chatDetail.setPinned(c.isPinned());
//        chatDetail.setTo_avatar(c.getTo_avatar());
//        chatDetail.setToName(c.getTo_name());
//        chatDetail.setToId(c.getTo_id());
//        repoChatDetail.save(chatDetail);
//        getMessage(shopeeId, Iconstants.test_key_customer_v2, Iconstants.partner_id_customer_v2, String.valueOf(conversation_id), shopId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    public void sendMessageQueue(SendValue sendValue) {
        System.out.println("sendValue"+ sendValue.getText());
        String access_token = shopeeService.getToken(sendValue.getShopeeId(), sendValue.getPartnerKeyV2(), sendValue.getPartnerIdV2(), 3L);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/sellerchat/send_message";
        Optional<ChatDetail> chatDetail = repoChatDetail.findByConversationIdAndShopInfoId(sendValue.getConversation_id(), sendValue.getShopId());
        Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(sendValue.getShopeeId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setTo_id(chatDetail.get().getToId());
        MessageContent messageContent = new MessageContent();
        ChatShopee chatShopee = new ChatShopee();
        if (sendValue.getText() != null) {
            sendMessage.setMessage_type("text");
            messageContent.setText(sendValue.getText());
            chatShopee.setContent(sendValue.getText());
        } else if (sendValue.getOrder() != null) {
            sendMessage.setMessage_type("order");
            messageContent.setOrder_sn(sendValue.getOrder());
            chatShopee.setContent(sendValue.getOrder());
        } else if (sendValue.getItem() != null) {
            sendMessage.setMessage_type("item");
            messageContent.setItem_id(sendValue.getItem());
            chatShopee.setContent(String.valueOf(sendValue.getItem()));
        }
        sendMessage.setContent(messageContent);
        ResponseDTO response = null;
        Map<String, Object> res = new HashMap<>();
        try {
            response = Common.callAPIV2(null, host, path, access_token, String.valueOf(sendValue.getShopeeId()), sendValue.getPartnerKeyV2(), String.valueOf(sendValue.getPartnerIdV2()), sendMessage, ResponseDTO.class);
            if (response != null && response.getResponse() != null) {
                Date date = new Date();
                chatShopee.setCreateDate(date);
//                chatShopee.setChatDetail(chatDetail.get());
                chatShopee.setMessageType(sendMessage.getMessage_type());
                chatShopee.setToId(chatDetail.get().getToId());
                chatShopee.setConversationId(sendValue.getConversation_id());
                chatShopee.setFromShopId(chatDetail.get().getShopId());
                chatShopee.setChannelShopee(channelShopee.get());
                chatShopee.setSender(1);
                ChatShopee data =  repoChatShopee.save(chatShopee);
                ChatDetail chatDetail1 = chatDetail.get();
                chatDetail1.setContent(sendValue.getText());
                chatDetail1.setMessage_type(chatShopee.getMessageType());
                chatDetail1.setOnView(false);
                repoChatDetail.save(chatDetail1);

                template.convertAndSend("/topic/sendMessageText/", data);
                res.put("success", true);
            }
            res.put("success", false);
        } catch (Exception e) {
            res.put("success", false);
        }
    }

    public void sendMessageImageQueue(QueueSendImage queueSendImage) {
        Long shopeeId = queueSendImage.getShopeeId();
        String partnerKey = queueSendImage.getPartnerKey();
        Long partnerID = queueSendImage.getPartnerID();
        String conversation_id = queueSendImage.getConversation_id();
        Long shopId = queueSendImage.getShopId();
        String file = queueSendImage.getFile();
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 3L);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/sellerchat/send_message";
        Optional<ChatDetail> chatDetail = repoChatDetail.findByConversationIdAndShopInfoId(conversation_id, queueSendImage.getShopId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setTo_id(chatDetail.get().getToId());
        MessageContent messageContent = new MessageContent();
        ChatShopee chatShopee = new ChatShopee();
        sendMessage.setMessage_type("image");
        messageContent.setImage_url(file);
        chatShopee.setContent(file);
        sendMessage.setContent(messageContent);
        int cnt = 0;
        ResponseDTO response = null;
        Map<String, Object> res = new HashMap<>();
        try {
            response = Common.callAPIV2(null, host, path, access_token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), sendMessage, ResponseDTO.class);
            Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopIdAndShopId(queueSendImage.getShopeeId(),shopId);
            if (response != null && response.getResponse() != null) {
                Date date = new Date();
                chatShopee.setCreateDate(date);
//                chatShopee.setChatDetail(chatDetail.get());
                chatShopee.setMessageType(sendMessage.getMessage_type());
                chatShopee.setToId(chatDetail.get().getToId());
                chatShopee.setConversationId(queueSendImage.getConversation_id());
                chatShopee.setFromShopId(chatDetail.get().getShopId());
                chatShopee.setChannelShopee(channelShopee.get());
                chatShopee.setSender(1);
                ChatShopee data =  repoChatShopee.save(chatShopee);
                ChatDetail chatDetail1 = chatDetail.get();
                chatDetail1.setContent(file);
                chatDetail1.setMessage_type(chatShopee.getMessageType());
                chatDetail1.setOnView(false);
                chatDetail1.setMessage_type("image");
                repoChatDetail.save(chatDetail1);
                template.convertAndSend("/topic/sendMessageImage/", data);
                res.put("success", true);
            }
            res.put("success", false);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
        }
    }
    public String sendImage(Long shopeeId, String partnerKey, Long partnerID ,MultipartFile file, Long shopId,String conversation_id){
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 3L);
        String host = "https://partner.shopeemobile.com";
        String path_img = "/api/v2/sellerchat/upload_image";
        Optional<ChatDetail> chatDetail = repoChatDetail.findByConversationIdAndShopInfoId(conversation_id, shopId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setTo_id(chatDetail.get().getToId());
        sendMessage.setMessage_type("image");
        long timestamp = Common.getCurrentTime();
        String base = partnerID + path_img + timestamp + access_token + shopeeId;
        String sign = Common.hash256(partnerKey, base);
        String url = host + path_img + "?access_token=" + access_token + "&shop_id=" + shopeeId + "&partner_id=" + partnerID + "&sign=" + sign + "&timestamp=" + timestamp;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
            map.add("file", file.getResource());
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<ResponseImg> responseImg = Iconstants.restTemplate.exchange(url, HttpMethod.POST, entity, ResponseImg.class);
            return Objects.requireNonNull(responseImg.getBody()).getResponse().getUrl();
        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity<?> pinConversation(Long shopeeId, String partnerKey, Long partnerID, Long conversation_id) {
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 3L);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/sellerchat/pin_conversation";
        ObjectMapper mapper = new ObjectMapper();
        PinConversation pinConversation = new PinConversation();
        pinConversation.setConversation_id(conversation_id);
        int cnt = 0;
        String response = null;
        Map<String, Object> res = new HashMap<>();
        try {
            response = Common.callAPIV2(null, host, path, access_token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), pinConversation, String.class);
            Optional<ChatDetail> chatDetail = repoChatDetail.findByConversationId(String.valueOf(conversation_id));
            if (chatDetail.isPresent()) {
                ChatDetail chatDetail1 = chatDetail.get();
                chatDetail1.setPinned(true);
                repoChatDetail.save(chatDetail1);
                template.convertAndSend("/topic/pinConversation/", chatDetail1);
                res.put("success", false);
                res.put("data", mapper.writeValueAsString(response));
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            res.put("success", false);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("message", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> unpinConversation(Long shopeeId, String partnerKey, Long partnerID, Long conversation_id) {
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 3L);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/sellerchat/unpin_conversation";
        ObjectMapper mapper = new ObjectMapper();
        PinConversation pinConversation = new PinConversation();
        pinConversation.setConversation_id(conversation_id);
        int cnt = 0;
        String response = null;
        Map<String, Object> res = new HashMap<>();
        try {
            response = Common.callAPIV2(null, host, path, access_token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), pinConversation, String.class);
            Optional<ChatDetail> chatDetail = repoChatDetail.findByConversationId(String.valueOf(conversation_id));
            if (chatDetail.isPresent()) {
                ChatDetail chatDetail1 = chatDetail.get();
                chatDetail1.setPinned(false);
                repoChatDetail.save(chatDetail1);
                template.convertAndSend("/topic/unpinConversation/", chatDetail1);
                res.put("success", false);
                res.put("data", mapper.writeValueAsString(response));
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            res.put("success", false);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAllChatDetail(Long shopId) {
        Map<String, Object> res = new HashMap<>();
        try {
            List<ChatDetail> chatDetails = repoChatDetail.findAllByShopInfoIdOrderByLastMessageTimestampDesc(shopId);
            res.put("data", chatDetails);
            res.put("success", true);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("ex", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAllChatDetailPageable(Long shopId, Pageable pageable) {
        Map<String, Object> res = new HashMap<>();
        try {
            Page<ChatDetail> chatDetails = repoChatDetail.findAllByShopInfoIdOrderByLastMessageTimestampDesc(shopId, pageable);
            res.put("data", chatDetails.getContent());
            res.put("total", chatDetails.getTotalElements());
            res.put("data2",chatDetails.getContent().get(2).getChatTags());
            res.put("totalPage", chatDetails.getTotalPages());
            res.put("success", true);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("ex", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getMessageData(String conversationId, Long shopeeId) {
        Map<String, Object> res = new HashMap<>();
        try {
            Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeId);
            List<MessageDTO> messageDTO = new ArrayList<>();
            List<ChatShopee> chatShopees = repoChatShopee.findAllByConversationIdAndChannelShopeeIdOrderByIdDesc(conversationId, channelShopee.get().getId());
            for (ChatShopee c :
                    chatShopees) {
                MessageDTO message = new MessageDTO(c.getId(), c.getMessageType(), c.getConversationId(), c.getMessageId(), c.getContent(), c.getSender(), c.getCreateDate());
                messageDTO.add(message);
            }
            res.put("data", messageDTO);
            res.put("success", true);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("ex", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getMessageDataPageable(String conversationId, Long shopeeId, Pageable pageable) {
        Map<String, Object> res = new HashMap<>();
        try {
            Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeId);
            List<MessageDTO> messageDTO = new ArrayList<>();
            Page<ChatShopee> chatShopees = repoChatShopee.findAllByConversationIdAndChannelShopeeIdOrderByCreateDateDesc(conversationId, channelShopee.get().getId(), pageable);
            for (ChatShopee c :
                    chatShopees.getContent()) {
                MessageDTO message = new MessageDTO(c.getId(), c.getMessageType(), c.getConversationId(), c.getMessageId(), c.getContent(), c.getSender(), c.getCreateDate());
                messageDTO.add(message);
            }
            res.put("data", messageDTO);
            res.put("total", chatShopees.getTotalElements());
            res.put("totalPage", chatShopees.getTotalPages());
            res.put("success", true);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("success", false);
            res.put("ex", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    //    public ResponseEntity getListOrder(Long shopeeId){
//        Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeId);
//        Map<String,Object> res = new HashMap<>();
//        List<OrderShopee> orderShopeeList = new ArrayList<>();
//        if (channelShopee.isPresent()){
//            List<OrderShopee> orderShopees = repoOrderChannel.findByChannelShopeeIdAndPhone(channelShopee.get().getId());
//            for (OrderShopee o:orderShopees) {
//                OrderShopee orderShopee = new OrderShopee();
//                orderShopee.setId(o.getId());
//                orderShopee.setOrderStatus(o.getOrderStatus());
//                orderShopee.setItemId(o.getItemId());
//                orderShopee.setOrdersn(o.getOrdersn());
//                orderShopeeList.add(orderShopee);
//            }
//            res.put("success",true);
//            res.put("data",orderShopeeList);
//            return new ResponseEntity(res,HttpStatus.OK);
//        }
//        res.put("success",false);
//        return new ResponseEntity(res,HttpStatus.BAD_REQUEST);
//    }
    public ResponseEntity<?> getListProduct(Long shopeeId, Long shopId) {
        Map<String, Object> res = new HashMap<>();
        try {
            List<ProductShopee> productShopees = repoProductShopee.findByShopeeIdAndShopId(shopeeId, shopId);
            List<DtoProductShopee> dtoProducts = new ArrayList<>();
            for (ProductShopee p : productShopees) {
                DtoProductShopee dtoProductShopee = new DtoProductShopee();
                Product product = p.getProduct();
                dtoProductShopee.setShopeeId(shopeeId);
                dtoProductShopee.setChannel(product.getChannel());
                dtoProductShopee.setImages(product.getImages());
                dtoProductShopee.setPrice(product.getPrice());
                dtoProductShopee.setSaleprice(product.getSaleprice());
                dtoProductShopee.setItemId(p.getItemId());
                dtoProductShopee.setTitle(product.getTitle());
                dtoProducts.add(dtoProductShopee);
            }
            res.put("success", true);
            res.put("data", dtoProducts);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            res.put("message", e.getMessage());
            res.put("success", false);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getToName(String toName, Long shopInfoId) {
        Map<String, Object> res = new HashMap<>();
        try {
            List<ChatDetail> chatDetails = repoChatDetail.findByShopInfoIdAndToNameContaining(shopInfoId, toName);
            res.put("success", true);
            res.put("data", chatDetails);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getTotal( Long shopInfoId) {
        Map<String, Object> res = new HashMap<>();
        try {
            List<ChatDetail> chatDetails = repoChatDetail.findByShopInfoIdAndOnView(shopInfoId, true);
            res.put("success", true);
            res.put("data", chatDetails.size());
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> onView(Long id){
        Map<String, Object> res = new HashMap<>();
        try {
            Optional<ChatDetail> chatDetails = repoChatDetail.findById(id);
            if (chatDetails.isPresent()){
                chatDetails.get().setOnView(false);
                repoChatDetail.save(chatDetails.get());
                template.convertAndSend("/topic/messageView/", chatDetails.get().getId());
                res.put("success", true);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            res.put("success", false);
            res.put("message", "không tìm thấy chat");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> findByContent(String conversationId,String content){
        Map<String, Object> res = new HashMap<>();
        try {
            List<ChatShopee> chatShopees = repoChatShopee.findByConversationIdAndContentContaining(conversationId,content);
            if (chatShopees.size() > 0){
                List<ChatShopee> chatShopeeList = new ArrayList<>();
                for (ChatShopee c: chatShopees) {
                    ChatShopee chatShopee = new ChatShopee();
                    chatShopee.setMessageId(c.getMessageId());
                    chatShopee.setCreateDate(c.getCreateDate());
                    chatShopee.setContent(c.getContent());
                    chatShopee.setId(c.getId());
                    chatShopee.setSender(c.getSender());
                    chatShopee.setConversationId(c.getConversationId());
                    chatShopee.setFromId(c.getFromId());
                    chatShopee.setMessageType(c.getMessageType());
                    chatShopee.setToId(c.getToId());
                    chatShopee.setToShopId(c.getToShopId());
                    chatShopeeList.add(chatShopee);
                }
                res.put("data",chatShopeeList);
                res.put("success", true);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            res.put("success", false);
            res.put("message", "không tìm thấy chat");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

//    public ResponseEntity<?> getContent(String content,String conversationId){
//
//    }


    public List<DtoOrderShopee> getBillOfShopByQuery(Long shopeeId,String toId, String query)  {
        List<DtoOrderShopee> dtoOrderShopees = new ArrayList<>();
        List<OrderShopee> orderShopees = repoOrderChannel.findByChannelShopeeShopeeShopIdAndBuyerUserIdAndQuery(shopeeId,toId,query);
        if (orderShopees.size() > 0) {
            return getDtoBills(dtoOrderShopees, orderShopees, toId);
        }
        return dtoOrderShopees;
    }

    public List<DtoOrderShopee> getBillOfShopByQueryPage(Long shopeeId,String toId, String query, Pageable pageable)  {
        List<DtoOrderShopee> dtoOrderShopees = new ArrayList<>();
        List<OrderShopee> orderShopees = repoOrderChannel.findByChannelShopeeShopeeShopIdAndBuyerUserIdAndQueryPage(shopeeId,toId,query, pageable);
        if (orderShopees.size() > 0) {
            return getDtoBills(dtoOrderShopees, orderShopees, toId);
        }
        return dtoOrderShopees;
    }

    public List<DtoOrderShopee> getBillOfShop(Long shopeeId,String toId) {
        List<DtoOrderShopee> dtoOrderShopees = new ArrayList<>();
        List<OrderShopee> orderShopees = repoOrderChannel.findByChannelShopeeShopeeShopIdAndBuyerUserId(shopeeId,toId);
        return getDtoBills(dtoOrderShopees, orderShopees, toId);
    }

    public List<DtoOrderShopee> getBillOfShopPage(Long shopeeId,String toId, Pageable pageable) {
        List<DtoOrderShopee> dtoOrderShopees = new ArrayList<>();
        List<OrderShopee> orderShopees = repoOrderChannel.findByChannelShopeeShopeeShopIdAndBuyerUserIdPage(shopeeId,toId, pageable);
        return getDtoBills(dtoOrderShopees, orderShopees, toId);
    }

    private List<DtoOrderShopee> getDtoBills(List<DtoOrderShopee> dtoOrderShopees, List<OrderShopee> orderShopees, String toId) {
            for (OrderShopee o: orderShopees) {
                DtoOrderShopee dtoOrderShopee = new DtoOrderShopee();
                dtoOrderShopee.setOrdersn(o.getOrdersn());
                dtoOrderShopee.setId(o.getId());
                dtoOrderShopee.setOrderStatus(o.getOrderStatus());
                dtoOrderShopee.setUpdateTime(o.getUpdateTime());
                dtoOrderShopee.setCreateDate(o.getBill().getCreatedDate());
                dtoOrderShopee.setShopShipMoney(o.getBill().getShopShipMoney());
                dtoOrderShopee.setNote(o.getBill().getInnerNote());
                dtoOrderShopee.setBuyerUserId(toId);
                List<ResponseBillDetail> responseBillDetail = new ArrayList<>();
                for (BillDetail b:o.getBill().getBillDetails()) {
                    ResponseBillDetail billDetail = new ResponseBillDetail();
                    billDetail.setImage(b.getVariant().getImage());
                    billDetail.setQuantity(b.getQuantity());
                    billDetail.setPrice(b.getPrice());
                    billDetail.setMoney(b.getMoney());
                    billDetail.setProductName(b.getProduct().getTitle());
                    billDetail.setProperties(b.getProperties());
                    responseBillDetail.add(billDetail);
                }
                dtoOrderShopee.setBillDetail(responseBillDetail);
                dtoOrderShopees.add(dtoOrderShopee);
            }
        return dtoOrderShopees.stream()
                .sorted(Comparator.comparing(DtoOrderShopee::getId).reversed())
                .collect(Collectors.toList());
    }


}
