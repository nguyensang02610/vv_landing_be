package com.vvlanding.shopee.order.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderDetailsResponseDTO {

    @JsonProperty("orders")
    private List<OrderDetail> orders = new ArrayList<>();

    private OrderResponse response;

    private String[] errors;
    private String request_id;
    private boolean success;

}
