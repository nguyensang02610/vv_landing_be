package com.vvlanding.shopee.item;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Item {
    private long item_id;
    private long shopid;
    private String name;
    private String description;
    private String status;
    private String item_sku;
    private List<String> images = new ArrayList<>();
    private String currency;
    private boolean has_variation;
    private double price;
    private int stock;
    private long create_time;

    private long update_time;

    private double weight;

    private long category_id;
    private double original_price;

    private List<Variation> variations = new ArrayList<>();

    private List<Attribute> attributes = new ArrayList<>();

    private Logistic[] logistics;
    private Wholesale[] wholesales;
    private double rating_star;
    private int cmt_count;
    private int sales;
    private int views;
    private int likes;

    private double package_length;
    private double package_width;
    private double package_height;
    private int days_to_ship;
    private String size_chart;
    private String condition; // NEW/LIKENEW/ USED
    private long discount_id;
    private boolean is_2tier_item;
    private List<Integer> tenures;
    private int reserved_stock;
    private boolean is_pre_order;
    private double inflated_price;
    private double inflated_original_price;
}
