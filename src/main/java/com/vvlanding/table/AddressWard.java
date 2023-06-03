package com.vvlanding.table;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address_wards")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class AddressWard {

    @Id
    @Column(name = "ward_id", length = 10)
    private String id;

    private String name;

    private String code;

}
