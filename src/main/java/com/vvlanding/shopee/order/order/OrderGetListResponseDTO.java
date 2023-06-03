package com.vvlanding.shopee.order.order;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderGetListResponseDTO {
    private List<Orders> orders = new ArrayList<>();
    private boolean more;
    private String request_id;
}
