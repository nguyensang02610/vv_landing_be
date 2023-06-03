package com.vvlanding.shopee.item.v2;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModelItem {

    private List<PriceInfo> price_info = new ArrayList<>();

    private Long model_id;

    private List<StockInfo> stock_info = new ArrayList<>();

    private String model_sku;
}
