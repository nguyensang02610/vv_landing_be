package com.vvlanding.shopee.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseRefresh_Token {
    private String request_id;
    private String message;
    private String error;
    private String refresh_token;
    private String access_token;
    private int expire_in;

}
