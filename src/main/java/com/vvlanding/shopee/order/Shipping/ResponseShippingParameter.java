package com.vvlanding.shopee.order.Shipping;

import com.vvlanding.shopee.order.ResponseOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseShippingParameter{
    private String error;
    private String message;
    private ShippingParameter response;
}
