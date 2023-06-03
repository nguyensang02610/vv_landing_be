package com.vvlanding.table;

import lombok.*;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "productCategories", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "title"
        })})

public class ProductCategories {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")// tên
    private String title;

//    @JsonIgnore
//    @OneToMany(mappedBy = "productCategories", fetch = FetchType.LAZY)
//    private Set<Product> products;

}
