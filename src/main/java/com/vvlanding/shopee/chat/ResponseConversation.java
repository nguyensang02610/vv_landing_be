package com.vvlanding.shopee.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseConversation {
    private String error;
    private String message;
    private String request_id;

    private Conversations response;
}
