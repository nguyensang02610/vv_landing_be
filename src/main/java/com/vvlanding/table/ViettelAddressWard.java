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
@Table(name = "viettel_ward")
public class ViettelAddressWard {

    @Id
    @Column(name = "wards_id")
    @JsonProperty("WARDS_ID")
    private int wardsId;

    @Column(name = "wards_name")
    @JsonProperty("WARDS_NAME")
    private String wardsName;

    @JoinColumn(name = "district_id")
    @JsonProperty("DISTRICT_ID")
    private int districtId;
}
