package com.vvlanding.dto;

import com.vvlanding.table.*;
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
public class DtoProductVariations {
    private Long id;
    private Long productId;
    private String productSku;
    private String productName;
    private String channel;
    private String sku;
    private String image;
    private String barcode;
    private Double price;
    private Double saleprice;
    private Boolean isActive;
    private String quantity;
    private Double weight;
    private List<Properties> properties = new ArrayList<>();


    public DtoProductVariations(long id, String sku, List<Properties> properties, String quantity, Double weight, Double price, String barcode, Double saleprice, String image, Boolean isActive) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
        this.properties = properties;
        this.weight = weight;
        this.price = price;
        this.image = image;
        this.barcode = barcode;
        this.saleprice = saleprice;
        this.isActive = isActive;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(Double saleprice) {
        this.saleprice = saleprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }


    public List<Properties> getProperties() {
        return properties;
    }

    public void setProperties(List<Properties> properties) {
        this.properties = properties;
    }
}
