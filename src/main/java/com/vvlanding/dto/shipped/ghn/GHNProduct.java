package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GHNProduct {

    private String name;

    private String code;

    private int quantity;

    private int price;

    private int length;

    private int width;

    private int height;

    @JsonProperty("variantId")
    private Long variantId;

    private String properties;

    private String channel;

    @JsonProperty("category")
    private GHNCategory category;

}
