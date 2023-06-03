package com.vvlanding.shopee.item.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockInfo {

    @JsonProperty("current_stock")
    private int current_stock;

    private int stock_type;
}
