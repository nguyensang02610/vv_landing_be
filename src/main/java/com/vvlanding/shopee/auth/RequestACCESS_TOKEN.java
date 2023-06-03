package com.vvlanding.shopee.auth;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class RequestACCESS_TOKEN {
    private String code;

    private Long partner_id;

    private Long shop_id;

    private int main_account_id;

    private String refresh_token;

    public RequestACCESS_TOKEN(String code, Long shop_id, Long partner_id) {
        this.code = code;
        this.shop_id = shop_id;
        this.partner_id = partner_id;
    }

    public RequestACCESS_TOKEN(Long shop_id, String refresh_token,Long partner_id) {
        this.shop_id =shop_id;
        this.refresh_token = refresh_token;
        this.partner_id = partner_id;
    }
}
