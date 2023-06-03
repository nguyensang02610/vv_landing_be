package com.vvlanding.shopee.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemDetailRequestDTO {
    @JsonProperty("partner_id")
    private long partnerId;

    @JsonProperty("shopid")
    private long shopid;

    private long timestamp;
    private long item_id;
}
