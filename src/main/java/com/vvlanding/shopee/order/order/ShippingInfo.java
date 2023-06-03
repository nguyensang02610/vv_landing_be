package com.vvlanding.shopee.order.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingInfo {
    private String shipping_carrier;
    private String tracking_number;
    private RecipientAddress recipient_address;
    private RecipientSortCode recipient_sort_code;
}
