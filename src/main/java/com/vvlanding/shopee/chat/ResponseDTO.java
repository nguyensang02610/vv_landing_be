package com.vvlanding.shopee.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    private String error;
    private String message;
    private String request_id;
    private boolean success;

    @JsonProperty("response")
    private Response response;
}
