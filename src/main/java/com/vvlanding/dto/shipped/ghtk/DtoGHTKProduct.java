package com.vvlanding.dto.shipped.ghtk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtoGHTKProduct {
    @NotNull
    private String name;

    private Double price;

    @NotNull
    private Double weight;

    private int quantity;

    @JsonProperty("product_code")
    private Integer productCode;

    @JsonProperty("variantId")
    private Long variantId;

    private String properties;

    private String channel;
}
