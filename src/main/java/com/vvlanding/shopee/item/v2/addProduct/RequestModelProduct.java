package com.vvlanding.shopee.item.v2.addProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestModelProduct {
    private Long item_id;
    private List<TierVariation> tier_variation = new ArrayList<>();
    private List<Model> model = new ArrayList<>();


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TierVariation{
        private String name;
        private List<RequestOptionList> option_list = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model{
        private List<Integer> tier_index = new ArrayList<>();
        private int normal_stock;
        private double original_price;
        private String model_sku;
    }
}
