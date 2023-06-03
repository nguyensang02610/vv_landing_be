package com.vvlanding.shopee.order.order;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    List<OrderDetailV2> order_list = new ArrayList<>();
}
