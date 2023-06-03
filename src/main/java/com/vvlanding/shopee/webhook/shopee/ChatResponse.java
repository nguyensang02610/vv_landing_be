package com.vvlanding.shopee.webhook.shopee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vvlanding.shopee.chat.MessageContent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {
    private String message_id;
    private int from_id;
    private String from_user_name;
    private int to_id;
    private long shop_id;
    private String to_user_name;
    private String message_type;
    private MessageContent content;
    private Long conversation_id;
    private Long created_timestamp;

}
