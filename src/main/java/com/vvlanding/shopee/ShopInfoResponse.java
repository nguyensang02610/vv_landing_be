package com.vvlanding.shopee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopInfoResponse {
    private String shop_name;
    private String region;
    private String status;

    private Object[] sip_affi_shops;
}
