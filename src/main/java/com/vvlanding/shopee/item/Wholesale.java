package com.vvlanding.shopee.item;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Wholesale {
    int min;
    int max;

    double unit_price;
}
