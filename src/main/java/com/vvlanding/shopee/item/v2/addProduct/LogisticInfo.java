package com.vvlanding.shopee.item.v2.addProduct;

import lombok.Data;

@Data
public class LogisticInfo {
    private int size_id;
    private double shipping_fee;
    private boolean enabled;
    private Long logistic_id;
    private boolean is_free = false;
}
