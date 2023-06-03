package com.vvlanding.shopee.item.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemResponse{

    private int total_count;

    @JsonProperty("has_next_page")
    private boolean hasNextPage;
    private int next_offset;

    @JsonProperty("item")
    private ItemV2[] itemV2;


}
