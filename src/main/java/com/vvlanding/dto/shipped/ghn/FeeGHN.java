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
public class FeeGHN {

    @JsonProperty("service_id")
    private int serviceId;

    @JsonProperty("service_type_id")
    private int serviceTypeId;

    @JsonProperty("insurance_value")
    private int insuranceValue;

    @JsonProperty("insurance_fee")
    private int insuranceFee;

    private int coupon;

    @JsonProperty("from_district_id")
    private int fromDistrictId;

    @JsonProperty("from_district_name")
    private String fromDistrictName;

    @JsonProperty("from_province_name")
    private String fromFromProvinceName;

    @JsonProperty("to_ward_code")
    private String toWardCode;

    @JsonProperty("to_ward_name")
    private String  toWardName;

    @JsonProperty("to_district_id")
    private int toDistrictId;

    @JsonProperty("to_district_name")
    private String toDistrictName;

    private String toProvince;

    private int weight;

    private int length;

    private int width;

    private int height;

}
