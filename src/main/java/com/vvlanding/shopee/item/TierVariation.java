package com.vvlanding.shopee.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vvlanding.shopee.item.v2.OptionList;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class TierVariation {
    private String name;
    private List<String> options;
    private List<String> images_url = new ArrayList<>();

    @JsonProperty("option_list")
    private List<OptionList> option_list = new ArrayList<>();
}
