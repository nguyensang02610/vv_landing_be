package com.vvlanding.control.controlpublic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import com.vvlanding.dto.QueueChatResponseData;
import com.vvlanding.dto.shipped.ghn.GHNWebhook;
import com.vvlanding.dto.shipped.ghtk.GHTKWebhook;
import com.vvlanding.dto.shipped.viettel.ViettelWebhook;
import com.vvlanding.repo.RepoCheckWebHook;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.shopee.webhook.shopee.*;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.service.SerGHN;
import com.vvlanding.service.SerGhtkService;
import com.vvlanding.service.SerViettel;
import com.vvlanding.table.CheckWebhook;
import org.apache.commons.codec.binary.Hex;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@RestController
@RequestMapping("api/webhook")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContWebhook {

    private ObjectMapper objectMapper = new ObjectMapper();

    private String partnerKey = Iconstants.partner_key;

    @Autowired
    RepoCheckWebHook repoCheckWebHook;

    @Autowired
    private SerGHN serGHN;

    @Autowired
    WebhookShopee webhookShopee;

    @Autowired
    SerGhtkService serGhtkService;

    @Autowired
    SerViettel serViettel;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    SimpMessagingTemplate templateSocket;

    @RequestMapping(value = "/ghn", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity webhookGHN(@RequestBody String webhook) throws JsonProcessingException {
        CheckWebhook checkWebhook = new CheckWebhook();
        checkWebhook.setCreateDate(new Date());
        checkWebhook.setBody(webhook);
        repoCheckWebHook.save(checkWebhook);
        GHNWebhook ghnWebhook = objectMapper.readValue(webhook, GHNWebhook.class);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        String IdBill =ghnWebhook.getOrderCode();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("statusGHN").document(IdBill).set(ghnWebhook);
        serGHN.webhook(ghnWebhook);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/ghtk",method = RequestMethod.POST)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> webhook(@RequestBody GHTKWebhook webhook){
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serGhtkService.webhook(webhook));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage,HttpStatus.OK);
    }

    @RequestMapping(value = "/ghvt",method = RequestMethod.POST)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> webhook(@RequestBody ViettelWebhook webhook, @RequestHeader String token){
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serViettel.webhook(webhook,token));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

//    @RequestMapping(value = "/update/status",method = RequestMethod.POST)
//    public ResponseEntity updateStatus(@RequestBody OrderStatusUpdate update) {
//
//        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",webhookShopee.hookOtherStatusOrder(update));
//        return new ResponseEntity(statusMessage,HttpStatus.OK);
//    }

    @RequestMapping(value = "/received",method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody String body){
        try {
//            CheckWebhook checkWebhook = new CheckWebhook();
//            checkWebhook.setCreateDate(new Date());
//            checkWebhook.setBody(body);
//            repoCheckWebHook.save(checkWebhook);
            WebhookPush webhookPush = objectMapper.readValue(body, WebhookPush.class);
            int code = webhookPush.getCode();
            switch (code) {
                case 3:
                    OrderStatusUpdate shopeeOrderStatus = objectMapper.readValue(body, OrderStatusUpdate.class);
                        if (shopeeOrderStatus != null) {
                            webhookShopee.hookOtherStatusOrder(shopeeOrderStatus);
                        }
                        System.out.println(objectMapper.writeValueAsString(shopeeOrderStatus));
                    break;
                case 4:
                    TrackingNoStatus trackingStatus = objectMapper.readValue(body, TrackingNoStatus.class);
                    if (trackingStatus != null && trackingStatus.getData() != null) {
                        String ordersn = trackingStatus.getData().getOrdersn();
                        String trackingno = trackingStatus.getData().getTrackingno();
                        long timestamp = webhookPush.getTimestamp();
                        webhookShopee.hookTrackingNo(ordersn, timestamp, trackingno);
                    }
                    break;
                case 10:
                    ChatResponseData chatResponseData = objectMapper.readValue(body, ChatResponseData.class);

                    if (chatResponseData != null && chatResponseData.getData() != null) {
                        Long shopId = webhookPush.getShop_id();
                        ChatResponse chatResponse = chatResponseData.getData().getContent();
                        String conversation_id = String.valueOf(chatResponse.getConversation_id());
                        QueueChatResponseData queueChatResponseData = new QueueChatResponseData();
                        queueChatResponseData.setChatResponse(chatResponse);
                        queueChatResponseData.setShopId(shopId);
                        queueChatResponseData.setConversation_id(conversation_id);
//                        template.convertAndSend(MQConfig.EXCHANGE_WEBHOOKSHOPEE,
//                                MQConfig.ROUTING_KEY_WEBHOOKSHOPEE, queueChatResponseData);
                        webhookShopee.hookChatShopee(chatResponse,conversation_id,shopId);
                    }
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity("",HttpStatus.OK);
    }
    @PostMapping(value = "/shopee")
    public ResponseEntity webhook(@RequestBody String body){
        System.out.println(body);
        return new ResponseEntity("",HttpStatus.OK);
    }

    public static Boolean verfiyPushMsg(String url, String requestBody, String partnerKey, String authorization)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, java.security.InvalidKeyException {

        String baseStr = url + "|" + requestBody;
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(partnerKey.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        String result = Hex.encodeHexString(sha256_HMAC.doFinal(baseStr.getBytes("UTF-8")));
        return result.equals(authorization);
    }

}
