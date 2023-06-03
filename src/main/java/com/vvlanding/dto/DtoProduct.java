package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DtoProduct {

    private Long id;
    private String title;
    private String sku;
    private String content;
    private Double price;
    private Double saleprice;
    private Long shopinfoId;
    private String unit;
    private long totalVari;
    private String channel;
    private List<String> tags;
    private List<String> images;
    private String quantity;
    private Boolean isActive;
    private Long productIdShopee;
    private Long shopeeId;
    private List<DtoProductVariations> variations = new ArrayList<>();

    public DtoProduct(Boolean isActive, long id, String sku, String name, Double originPrice, String unit, List<String> tags, Double salePrice,
                      List<DtoProductVariations> variants, String content, long shopid, List<String> images, long totalVari, String quantity, String channel) {
        this.id = id;
        this.sku = sku;
        this.title = name;
        this.variations = variants;
        this.unit = unit;
        this.images = images;
        this.tags = tags;
        this.content = content;
        this.shopinfoId = shopid;
        this.price = originPrice;
        this.saleprice = salePrice;
        this.totalVari = totalVari;
        this.quantity = quantity;
        this.isActive = isActive;
        this.channel = channel;
    }
}