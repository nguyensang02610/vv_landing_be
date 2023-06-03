package com.vvlanding.shopee.order.Shipping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDropoff {
    private Long branch_id;
    private String sender_real_name;
    private String tracking_number;
}
