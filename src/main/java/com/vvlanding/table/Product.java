package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")// tênSP
    private String title;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "sku")
    private String sku;

    @Column(name = "content", columnDefinition = "TEXT") // mô tả sp
    private String content;

    @Column(name = "price")// giá gốc
    private Double price;

    @Column(name = "saleprice")// giá bán
    private Double saleprice;

    @Column(name = "channel")
    private String channel;

//    @Column(name = "item_id") // id sp post product sang shopee
//    private Long itemId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ref_product_images")
    private List<String> images;

    // Shop 1 -n Product
    @ManyToOne
    @JoinColumn(name = "shopInfo_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShopInfo shopInfo;

    // Unit 1-n Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Unit_id")
    private Units units;

    // Categores 1-n Product
//    @ManyToOne
//    @JoinColumn(name = "Categores_id")
//    private ProductCategories productCategories;

    //-- Product 1-n ProductVariation
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<ProductVariations> variations;


    // -- Product 1-n BillDetails
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private Set<BillDetail> BillDetails;

    // n tags n product
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "ref_product_tag",
            joinColumns = @JoinColumn(
                    name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "product_tag_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<ProductTag> productTag;

}