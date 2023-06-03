package com.vvlanding.shopee.order.Shipping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSlotList {
    private Long date;
    private String time_text;
    private String pickup_time_id;
}
