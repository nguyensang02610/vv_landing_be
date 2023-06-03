package com.vvlanding.shopee.item.v2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {
    private Long category_id;

    private Long parent_category_id;

    private String original_category_name;

    private String display_category_name;

    private boolean has_children;
}
