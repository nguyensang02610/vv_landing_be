package com.vvlanding.shopee.auth;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ShopAuthReqDTO {
    private long partnerId;
    private String partnerKey;
    private String redirectUrl;

}
