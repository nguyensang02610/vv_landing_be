package com.vvlanding.shopee.item.v2.addProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseItem {
    private String error;
    private String message;
    private Item response;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Item{
        private Long item_id;
    }
}
