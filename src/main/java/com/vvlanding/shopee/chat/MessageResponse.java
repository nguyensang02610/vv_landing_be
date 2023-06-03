package com.vvlanding.shopee.chat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class MessageResponse {

    private String message_id;
    private String message_type;
    private int from_id;
    private int from_shop_id;
    private int to_id;
    private int to_shop_id;
    private String conversation_id;
    private String status;
    private String source;
    private Long created_timestamp;
    private MessageContent content;

}
