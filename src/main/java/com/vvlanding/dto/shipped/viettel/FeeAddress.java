package com.vvlanding.dto.shipped.viettel;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FeeAddress {
    @JsonProperty("PROVINCE_ID")
    private int provinceId;

    @JsonProperty("PROVINCE_CODE")
    private String provinceCode;

    @JsonProperty("PROVINCE_NAME")
    private String provinceName;
}
