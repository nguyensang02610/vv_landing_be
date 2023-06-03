package com.vvlanding.shopee.item.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceInfo {

    private String currency;
    private float original_price;
    private float current_price;

}
