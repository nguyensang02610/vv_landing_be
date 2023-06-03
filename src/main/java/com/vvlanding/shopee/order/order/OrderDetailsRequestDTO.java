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
public class OrderDetailsRequestDTO {
    @JsonProperty("partner_id")
    private long partnerId;

    @JsonProperty("shopid")
    private long shopid;

    private long timestamp;

    private List<String> ordersn_list = new ArrayList<>();

    public OrderDetailsRequestDTO(List<String> ordersn_list, long partnerID, long shopeeShopId, long timestamp) {
        this.ordersn_list = ordersn_list;
        this.partnerId = partnerID;
        this.shopid =shopeeShopId;
        this.timestamp = timestamp;
    }
}
