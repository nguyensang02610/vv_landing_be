package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GHNFeeOrder {

    @JsonProperty("main_service")
    private int mainService;

    @JsonProperty("insurance")
    private int insurance;

    @JsonProperty("station_do")
    private int stationDo;

    @JsonProperty("station_pu")
    private int stationPu;

    @JsonProperty("return")
    private int returnId;

    @JsonProperty("r2s")
    private int r2s;

    @JsonProperty("coupon")
    private int coupon;
}
