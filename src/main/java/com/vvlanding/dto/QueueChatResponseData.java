package com.vvlanding.dto;

import com.vvlanding.shopee.webhook.shopee.ChatResponse;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueueChatResponseData {
    private  String conversation_id;

    private  Long shopId;

    private ChatResponse chatResponse;

}
