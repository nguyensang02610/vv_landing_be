package com.vvlanding.table;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address_provinces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@JsonIgnoreProperties({ "districts"})
public class AddressProvince  implements Comparable<AddressProvince> {
    @Id
    @Column(name = "province_id", length = 10)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ref_province_districts")
    private List<AddressDistrict> districts;

    public AddressProvince(String provinceID, String provinceName, String provinceCode, Object o) {
    }

    @Override
    public int compareTo(AddressProvince o) {
        return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
    }
}
