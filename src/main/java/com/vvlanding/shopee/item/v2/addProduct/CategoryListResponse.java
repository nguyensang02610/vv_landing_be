package com.vvlanding.shopee.item.v2.addProduct;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryListResponse {
    List<CategoryList> category_list = new ArrayList<>();
}
