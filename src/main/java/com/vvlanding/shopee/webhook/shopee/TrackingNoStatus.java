package com.vvlanding.shopee.webhook.shopee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingNoStatus {
    public int code;
    public long timestamp;
    @JsonProperty("shop_id")
    public long shopId;
    public Data data;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        @JsonProperty("forder_id")
        public String forderId;
        public String ordersn;
        @JsonProperty("tracking_no")
        public String trackingno;
    }

}
