package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DtoChannelShopee {

    private long id;

    private long ShopId;

    private long shopeeShopId;

    private String shopeeShopName;

    private String shopeeShopDesc;

    private String shopeeShopImage;

    public void ChannelShopee(long id, Long ShopId, long shopeeShopId, String shopeeShopName,
                              String shopeeShopDesc, String shopeeShopImage) {
        this.id = id;
        this.ShopId = ShopId;
        this.shopeeShopId = shopeeShopId;
        this.shopeeShopName = shopeeShopName;
        this.shopeeShopDesc = shopeeShopDesc;
        this.shopeeShopImage = shopeeShopImage;
    }
}
