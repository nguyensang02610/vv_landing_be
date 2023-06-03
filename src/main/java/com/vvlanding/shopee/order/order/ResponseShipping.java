package com.vvlanding.shopee.order.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseShipping {

    private String error;
    private String message;

    private Shipping response;
}
