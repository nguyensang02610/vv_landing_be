package com.vvlanding.shopee.item;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Attribute {
    long attribute_id;
    long attributes_id;
    String attribute_name;
    boolean is_mandatory;
    String attribute_type;
    String attribute_value;
    String value;

}
