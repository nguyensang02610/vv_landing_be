package com.vvlanding.dto.shipped.ghtk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipPackageDTO {

    @JsonProperty("pick_province")
    private String pickProvince;

    @JsonProperty("pick_district")
    private String pickDistrict;

    private String province;
    private String district;
    private String address;
    private Integer weight;
    private Integer value;
    private String transport;

    @JsonProperty("deliver_option")
    private String deliverOption;
}
