package com.vvlanding.shopee.item.v2.addProduct;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AttributeList {
    private Long attribute_id;
    private List<AttributeValueList> attribute_value_list = new ArrayList<>();
}
