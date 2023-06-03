package com.vvlanding.shopee.order.order;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ItemOrder {
    String item_id;
    String item_name;
    String item_sku;
    long variation_id;
    String variation_name;
    Long model_id;
    String model_name;
    String model_sku;
    Long model_quantity_purchased;
    double model_original_price;
    double model_discounted_price;
    String variation_sku;
    long variation_quantity_purchased;
    double variation_original_price;
    double variation_discounted_price;
    boolean is_wholesale;

    double weight;
    boolean is_add_on_deal;
    boolean is_main_item;
    Long add_on_deal_id;
    String promotion_type;
    Long promotion_id;
}
