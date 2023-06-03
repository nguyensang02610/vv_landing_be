package com.vvlanding.shopee.item.v2.addProduct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestProduct {
    private Long productId;
    private Long logisticId;
    private Long categoryId;
    private int quantity;

}
