package com.vvlanding.shopee.order.Shipping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestShipOrder {
    private String ordersn;
    private RequestPickup pickup;
    private RequestDropoff dropoff;
}
