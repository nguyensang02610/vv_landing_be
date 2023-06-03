package com.vvlanding.shopee.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessage {
    private int to_id;
    private String message_type;
    private MessageContent content;
}
