package com.vvlanding.shopee.item.v2.addProduct;

import com.vvlanding.shopee.item.v2.Logistics;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AddProduct {
    private double original_price;
    private String description;
    private double weight;
    private String item_name;
    private String item_status;
    private Dimension dimension;
    private int normal_stock;
    private List<LogisticInfo> logistic_info = new ArrayList<>();
    private Long category_id;
    private String item_sku;
    private String condition;
    private Brand brand;
    private Long item_dangerous;
    private ImageId image;
    private AttributeList attribute_list;

}
