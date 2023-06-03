package com.vvlanding.shopee.item.v2.addProduct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryListDTO {
    private String error;
    private String message;
    private CategoryListResponse response;
}
