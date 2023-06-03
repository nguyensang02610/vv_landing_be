package com.vvlanding.shopee.item.v2;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ResponseCategory {
    List<Category> category_list = new ArrayList<>();
}
