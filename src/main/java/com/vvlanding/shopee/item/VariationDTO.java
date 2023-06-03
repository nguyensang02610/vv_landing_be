package com.vvlanding.shopee.item;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class VariationDTO {
    private long item_id;
    private List<TierVariation> tier_variation = new ArrayList<>();
    private List<Variations> variations = new ArrayList<>();
    private String request_id;
}
