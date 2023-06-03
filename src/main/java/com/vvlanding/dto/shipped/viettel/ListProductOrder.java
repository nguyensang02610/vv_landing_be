package com.vvlanding.dto.shipped.viettel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListProductOrder {

    @JsonProperty("PRODUCT_NAME")
    private String productName;

    @JsonProperty("PRODUCT_PRICE")
    private Long productPrice;

    @JsonProperty("PRODUCT_WEIGHT")
    private Long productWeight;

    @JsonProperty("PRODUCT_QUANTITY")
    private Long productQuantity;

    @JsonProperty("variantId")
    private Long variantId;

    private String properties;

    private String channel;

    private Long productCode;
}
