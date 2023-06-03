package com.vvlanding.dto.shipped.ghn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GHNFeeResponse {

    private int total;
    private int service_fee;
    private int insurance_fee;
    private int pick_station_fee;
    private int coupon_value;
    private int r2s_fee;
}
