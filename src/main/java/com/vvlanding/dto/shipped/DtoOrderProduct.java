package com.vvlanding.dto.shipped;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DtoOrderProduct {

    private Long id; // id để upd
    private Long productId;
    private Long variantId;
    private String productSku;
    private String productName;
    private String channel;
    private String sku;
    private String image;
    private String barcode;
    private Double price;
    private Double saleprice;
    private Boolean isActive;
    private Double inputQuantity;
    private Double weight;
    private String properties;
//    private List<Properties> properties = new ArrayList<>();


}
