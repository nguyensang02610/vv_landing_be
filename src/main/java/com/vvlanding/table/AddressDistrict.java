package com.vvlanding.table;

import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address_districts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"wards", "hibernateLazyInitializer"})
public class AddressDistrict {
    @Id
    @Column(name = "district_id", length = 10)
    private String id;

    private String name;

    private String code;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ref_district_wards")
    private List<AddressWard> wards;

    public AddressDistrict(String districtID, String districtName, String districtCode, Object o) {
    }
}
