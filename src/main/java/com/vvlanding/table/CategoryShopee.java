package com.vvlanding.table;

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
@Table(name = "category_shopee")
public class CategoryShopee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "parent_category_id")
    private Long parentCategoryId;

    @Column(name = "original_category_name")
    private String originalCategoryName;

    @Column(name = "display_category_name")
    private String displayCategoryName;

    @Column(name = "has_children")
    private boolean hasChildren;
}
