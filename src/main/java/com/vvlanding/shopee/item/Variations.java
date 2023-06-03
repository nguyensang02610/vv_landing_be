package com.vvlanding.shopee.item;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Variations {
    private String variation_id;
    private long[] tier_index;

}
