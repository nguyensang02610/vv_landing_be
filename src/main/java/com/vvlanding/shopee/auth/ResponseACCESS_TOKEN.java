package com.vvlanding.shopee.auth;

import lombok.*;

@Getter
@Setter
public class ResponseACCESS_TOKEN {
    private String request_id;

    private String error;

    private String refresh_token;

    private String access_token;

    private int expire_in;

    private String message;

    private int[] merchant_id_list;

    private int[] shop_id_list;
}
