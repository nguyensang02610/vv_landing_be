package com.vvlanding.shopee.order.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderGetListRequestDTO {
    @JsonProperty("partner_id")
    private long partnerId;

    @JsonProperty("shopid")
    private long shopid;

    private long timestamp;
//    private String order_status;
    private long create_time_from;
    private long create_time_to;
    private String order_status;


    public OrderGetListRequestDTO(long start, long end, long partnerID, long shopeeShopId, long timestamp) {
        this.create_time_from = start;
        this.create_time_to = end;
        this.partnerId = partnerID;
        this.shopid = shopeeShopId;
        this.timestamp = timestamp;
    }

    public OrderGetListRequestDTO(long partnerID, Long shopeeId, long apiTimeStamp, String all, long startTime, long endTime) {
        this.create_time_from = startTime;
        this.create_time_to = endTime;
        this.partnerId = partnerID;
        this.shopid = shopeeId;
        this.timestamp = apiTimeStamp;
        this.order_status = all;
    }
}
