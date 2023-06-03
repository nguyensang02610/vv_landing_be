package com.vvlanding.shopee.order.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Orders {
    private String ordersn;
    private String order_sn;
    private String order_status;
    private long update_time;
}
