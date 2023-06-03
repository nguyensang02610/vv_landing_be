package com.vvlanding.shopee.item.v2.addProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vvlanding.shopee.item.v2.GetLogistic;
import com.vvlanding.shopee.item.v2.LogisticsDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLogisticDTO {
    private String error;
    private String message;
    private LogisticsDTO response;
}
