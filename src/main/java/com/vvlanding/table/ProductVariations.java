package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "productVariations")// Sản phẩm biến thể

public class ProductVariations {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    private String image;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "sku")
    private String sku;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "price")// giá
    private Double price;

    @Column(name = "saleprice")// giá giam
    private Double saleprice;

    //-- Product 1-n ProductVariation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product products;


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "variant")
    private Set<BillDetail> BillDetails;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })

    @JoinTable(name = "product_properties",
            joinColumns = @JoinColumn(
                    name = "productVariation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "properties_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<Properties> properties;

}
