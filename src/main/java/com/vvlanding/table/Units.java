package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "units")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Units {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


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

    //   unit 1-n product
    @JsonIgnore
    @OneToMany(mappedBy = "units", fetch = FetchType.LAZY)
    private Set<Product> products;

}
