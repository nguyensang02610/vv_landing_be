package com.vvlanding.shopee.order.Shipping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingParameter {
    private InfoNeeded info_needed;
    private Dropoff dropoff;
    private Pickup pickup;
}
