package com.vvlanding.shopee.webhook.shopee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusUpdate {
    @JsonProperty("shop_id")
    public long shop_id;

    public int code;
    public Data data;

    public long timestamp;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        public String ordersn;
        public String status;
        public long shop_id;
        public int code;
        @JsonProperty("update_time")
        public long updateTime;
    }
}
