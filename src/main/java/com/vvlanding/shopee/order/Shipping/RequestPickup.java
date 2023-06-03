package com.vvlanding.shopee.order.Shipping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPickup {
    private Long address_id;
    private String pickup_time_id;
    private String tracking_number;
}
