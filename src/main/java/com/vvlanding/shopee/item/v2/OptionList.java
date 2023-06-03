package com.vvlanding.shopee.item.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionList {

    private String option;

    private String name;

    @JsonProperty("image")
    private ImageV2 image;

}
