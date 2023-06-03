package com.vvlanding.shopee.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListItem {

    private long item_id;

    @JsonProperty("shopid")
    private long shop_id;

    private int update_time;

    private String status;

    private String item_sku;

    private boolean is_2tier_item;
}
