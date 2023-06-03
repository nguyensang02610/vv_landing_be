package com.vvlanding.shopee.item;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Variation {
    String variation_sku;
    long variation_id;
    String name;
    double price;
    long stock;
    String status;
    long create_time;
    long update_time;
    double original_price;
    int discount_id;
    int reserved_stock;
    double inflated_priceAnInt;
    double inflated_price;
}
