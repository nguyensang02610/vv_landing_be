package com.vvlanding.shopee.order.Shipping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoNeeded {
    private String[] dropoff;

    private String[] pickup;

    private String[] non_integrated;
}
