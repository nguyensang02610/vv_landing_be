package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "viettel_district")
public class ViettelAddressDistrict {

    @Id
    @Column(name = "district_id")
    @JsonProperty("DISTRICT_ID")
    private int districtId;

    @Column(name = "district_value")
    @JsonProperty("DISTRICT_VALUE")
    private String districtValue;

    @Column(name = "district_name")
    @JsonProperty("DISTRICT_NAME")
    private String districtName;

    @Column(name = "province_id")
    @JsonProperty("PROVINCE_ID")
    private int provinceId;
}
