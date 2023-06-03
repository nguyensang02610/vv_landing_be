package com.vvlanding.shopee.item.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDetail {

        private Long item_id;
        private Long category_id;
        private String item_name;
        private String description;
        private String item_sku;
        private Long create_time;
        private Long update_time;
        private double weight;
        private String item_status;
//        private List<AttributeList> attribute_list;

        @JsonProperty("stock_info")
        private List<StockInfo> stock_info = new ArrayList<>();

        @JsonProperty("price_info")
        private PriceInfo[] price_info;

        @JsonProperty("image")
        private ImageV2 image;

        private VideoInfo[] video_info;
}
