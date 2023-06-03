package com.vvlanding.shopee.auth;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ShopAuthResDTO {
    private String authUrl;
    private long createTime;
}
