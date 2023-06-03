package com.vvlanding.shopee.webhook.shopee;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import com.vvlanding.dto.QueueChatResponseData;
import com.vvlanding.shopee.order.order.OrderServices;
import com.vvlanding.shopee.order.order.ResponseShipping;
import com.vvlanding.shopee.service.shopee.ChatService;
import com.vvlanding.repo.*;
import com.vvlanding.table.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class WebhookShopee {

    @Autowired
    RepoOrderChannel repoOrderChannel;

    @Autowired
    RepoBill repoBill;

    @Autowired
    RepoChannelShopee repoChannelShopee;

    @Autowired
    OrderServices orderServices;

    @Autowired
    RepoChatDetail repoChatDetail;

    @Autowired
    RepoChatShopee repoChatShopee;

    @Autowired
    ChatService chatService;

    @Autowired
    RepoStatus repoStatus;

    @Autowired
    SimpMessagingTemplate template;

    public void hookOtherStatusOrder(OrderStatusUpdate update){
        Optional<OrderShopee> findByShopeeOrdersn = repoOrderChannel.findByOrdersn(update.data.getOrdersn());
        Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(update.getShop_id());
        OrderStatusUpdate.Data dtoOrder = update.getData();
        String status = MapOrderStatus.map(update.data.getStatus(), ChannelSources.SHOPEE);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        String IdBill = dtoOrder.getOrdersn();
        dtoOrder.setStatus(status);
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("orderShopee").document(IdBill).set(dtoOrder);
        if (findByShopeeOrdersn.isPresent()) {
            Bill bill = findByShopeeOrdersn.get().getBill();
            OrderShopee order = findByShopeeOrdersn.get();

            if (bill.getOrderCode() == null){
                try {
                    ResponseShipping shipping = orderServices.getShipping(update.data.getOrdersn(),update.getShop_id());
                    bill.setOrderCode(shipping.getResponse().getShipping_document_info().getTracking_number()+"/"+shipping.getResponse().getShipping_document_info().getRecipient_sort_code().getFirst_recipient_sort_code());
                }catch (Exception e){

                }
            }
            Optional<StatusShipped> statusShipped = repoStatus.findByStatusName(status);
            order.setOrderStatus(status);
            order.setUpdateTime(new Date());
            bill.setStatus(status);
            bill.setStatusShipped(statusShipped.get());
            repoBill.save(bill);
            repoOrderChannel.save(order);

            template.convertAndSend("/topic/webhookShopeeBill1/"+ bill.getShop().getId(), bill.getId());
        } else {
            List<String> ordersn_list = new ArrayList<>();
            ordersn_list.add(update.data.getOrdersn());
            channelShopee.ifPresent(shopee -> orderServices.getDetailV2(shopee.getShop(), update.getShop_id(), ordersn_list));
        }
    }

    public void hookTrackingNo(String ordersn, long updateTime, String trackingNo) {
        Optional<OrderShopee> findByShopeeOrdersn = repoOrderChannel.findByOrdersn(ordersn);
        if (findByShopeeOrdersn.isPresent()) {
            OrderShopee order = findByShopeeOrdersn.get();
            order.setTrackingNo(trackingNo);
            Date modifiedDate = new Date(updateTime * 1000);
            order.setUpdateTime(modifiedDate);
            repoOrderChannel.save(order);
        }
    }

//    @RabbitListener(queues = MQConfig.QUEUE_WEBHOOKSHOPEE)
//    public void hookChatShopee(QueueChatResponseData queueChatResponseData){
    public void hookChatShopee(ChatResponse chatResponse, String conversation_id,Long shopId){
//        String conversation_id = queueChatResponseData.getConversation_id();
//        Long shopId = queueChatResponseData.getShopId();
//        ChatResponse chatResponse = queueChatResponseData.getChatResponse();
        Optional<ChatShopee> chatShopee = repoChatShopee.findByMessageId(chatResponse.getMessage_id());
        Optional<ChatDetail> chatDetail = repoChatDetail.findByConversationId(conversation_id);
        if (!chatShopee.isPresent()){
            Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopId);
            ChatShopee chat = new ChatShopee();
            if (chatResponse.getMessage_type().contains("text")){
                chat.setContent(chatResponse.getContent().getText());
            }else if (chatResponse.getMessage_type().contains("sticker")){
                chat.setContent(chatResponse.getContent().getSticker_id());
            }else if (chatResponse.getMessage_type().contains("image")){
                chat.setContent(chatResponse.getContent().getUrl());
            }else if (chatResponse.getMessage_type().contains("order")){
                chat.setContent(chatResponse.getContent().getOrder_sn());
            }else if (chatResponse.getMessage_type().contains("item")){
                chat.setContent(chatResponse.getContent().getText());
            }
            long time = chatResponse.getCreated_timestamp()*1000;
            Date date =  new Date(time);
            if (!chatDetail.isPresent()){
                ChatDetail chatDetail1 = new ChatDetail();
                chatDetail1.setOnView(true);
                chatDetail1.setLastMessageTimestamp(date);
                chatDetail1.setShopId(shopId.intValue());
                chatDetail1.setMessage_type(chatResponse.getMessage_type());
                chatDetail1.setToName(chatResponse.getFrom_user_name());
                chatDetail1.setToId(chatResponse.getFrom_id());
                chatDetail1.setConversationId(conversation_id);
                chatDetail1.setShopInfoId(channelShopee.get().getShop().getId());
                chat.setChatDetail(repoChatDetail.save(chatDetail1));
            }else {
//                chat.setChatDetail(chatDetail.get());
                ChatDetail chatDetail1 = chatDetail.get();
                chatDetail1.setOnView(true);
                chatDetail1.setContent(chat.getContent());
                chatDetail1.setMessage_type(chat.getMessageType());
                chatDetail1.setLastMessageTimestamp(date);
                repoChatDetail.save(chatDetail1);
            }
            chat.setCreateDate(date);
            chat.setMessageId(chatResponse.getMessage_id());
            chat.setFromId(shopId.intValue());
            chat.setFromShopId(shopId.intValue());
            chat.setToId(chatResponse.getFrom_id());
            chat.setMessageType(chatResponse.getMessage_type());
            chat.setChannelShopee(channelShopee.get());
            chat.setConversationId(conversation_id);
            chat.setSender(0);
            repoChatShopee.save(chat);

            template.convertAndSend("/topic/webhookShopeeChat/", chat);

            Firestore dbFirestore = FirestoreClient.getFirestore();
//            String id = String.valueOf(chat.getId());
            String id = chatResponse.getMessage_id();
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("messShopee").document(id).set(chatResponse);

        }
    }
}
