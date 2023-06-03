package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "viettel_province")
public class ViettelAddressProvince {

    @Id
    @Column(name = "id")
    @JsonProperty("PROVINCE_ID")
    private int provinceId;

    @Column(name = "code")
    @JsonProperty("PROVINCE_CODE")
    private String provinceCode;

    @Column(name = "name")
    @JsonProperty("PROVINCE_NAME")
    private String provinceName;

}
