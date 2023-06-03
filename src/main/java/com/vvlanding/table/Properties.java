package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "properties")
public class Properties {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "keyname")
    private String keyname;

    @Column(name = "value")
    private String value;

    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date createdDate = new Date();

    @Basic(optional = false)
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date modifiedDate = new Date();

    @Column(name = "is_active", columnDefinition = "boolean default 1")
    @JsonIgnore
    private boolean active;

    //-- properties 1-n BillDetail
    //    @JsonIgnore
    //    @OneToMany(fetch = FetchType.LAZY, mappedBy = "properties")
    //    private Set<BillDetail> listBillDetail;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "properties")
    @JsonIgnore
    private Set<ProductVariations> productVariations = new HashSet<>();

    public Properties(String keyname, String value, Date createdDate, long id, Date modifiedDate) {
        this.keyname = keyname;
        this.value = value;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.id = id;
    }

}
