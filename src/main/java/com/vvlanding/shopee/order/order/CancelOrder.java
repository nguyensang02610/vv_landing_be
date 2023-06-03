package com.vvlanding.shopee.order.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelOrder {

    private String ordersn;

    private String cancel_reason;

    private int item_id;

    private int variation_id;

    private Long partner_id;

    private Long shopid;

    private Long timestamp;
}
