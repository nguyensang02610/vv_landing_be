package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DtoOrderDetail {
    private Long id;
    private Long productId;
    private Long variantId;
    private Double quantity;
    private Double price;
    private Double money;
    private String productTitle;
    private String properties;
    private String channel;
    private Double weightProduct;
    private List productImages;

}
