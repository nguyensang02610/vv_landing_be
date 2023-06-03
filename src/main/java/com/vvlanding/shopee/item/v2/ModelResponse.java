package com.vvlanding.shopee.item.v2;

import com.vvlanding.shopee.item.TierVariation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ModelResponse {

    private List<TierVariation> tier_variation = new ArrayList<>();

    private List<ModelItem> model = new ArrayList<>();
}
