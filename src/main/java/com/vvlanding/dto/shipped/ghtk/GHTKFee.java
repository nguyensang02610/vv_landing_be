package com.vvlanding.dto.shipped.ghtk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GHTKFee {
    private String name;

    private int fee;

    @JsonProperty("insurance_fee")
    private int insuranceFee;

    @JsonProperty("delivery_type")
    private String deliveryType;

    private String a;

    private String dt;

    private boolean delivery;
}
