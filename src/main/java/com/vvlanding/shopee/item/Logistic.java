package com.vvlanding.shopee.item;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Logistic {
    long logistic_id;
    String logistic_name;
    boolean enabled;

    double shipping_fee;
    long size_id;

    boolean is_free;
    double estimated_shipping_fee;
}
