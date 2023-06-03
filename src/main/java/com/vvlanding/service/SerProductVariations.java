package com.vvlanding.service;

import com.vvlanding.dto.*;

import com.vvlanding.mapper.MapperProductVariations;
import com.vvlanding.payload.ResponseStatus;
import com.vvlanding.repo.RepoProductVariations;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.table.Product;
import org.springframework.data.domain.Pageable;

import java.util.*;

import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SerProductVariations {

    @Autowired
    RepoProductVariations repoProductVariations;

    @Autowired
    MapperProductVariations mapperProductVariations;

    @Autowired
    SerProperties serProperties;

    @Autowired
    RepoShopInfo repoShopInfo;


    public void save(List<ProductVariations> productVariations) {
        repoProductVariations.saveAll(productVariations);
    }


    public Optional<ProductVariations> find(long id) {
        return repoProductVariations.findById(id);
    }

    Map<String, Object> response = new HashMap<>();

    public List<DtoProductVariations> getAllProductVariantions(Pageable pageable) {
        List<ProductVariations> productVariations = repoProductVariations.findAllBy(pageable);
        List<DtoProductVariations> dtoProductVariations = new ArrayList<>();
        for (ProductVariations productVariations1 : productVariations) {
            DtoProductVariations dtoProductVariations1 = mapperProductVariations.toDto(productVariations1);
//            dtoProductVariations1.setDtoProperties(new ArrayList<>(serProperties.getAllPropertiesByProductVariationsId(productVariations1)));
            dtoProductVariations.add(dtoProductVariations1);
        }
        return dtoProductVariations;
    }

    public List<DtoProductVariations> getAllProductVariantionByProductId(Product product) {
        List<ProductVariations> productVariations = repoProductVariations.findAllByProducts(product);
        List<DtoProductVariations> dtoProductVariations = new ArrayList<>();
        for (ProductVariations productVariations1 : productVariations) {
            DtoProductVariations dtoProductVariations1 = mapperProductVariations.toDto(productVariations1);
//            dtoProductVariations1.setDtoProperties(new ArrayList<>(serProperties.getAllPropertiesByProductVariationsId(productVariations1)));
            dtoProductVariations.add(dtoProductVariations1);
        }
        return dtoProductVariations;

    }
    public List<ProductVariations> findByProduct(Product product) {
        return repoProductVariations.findByProducts(product);
    }

    public List<ProductVariations> findByProductId(Long productId) {
        return repoProductVariations.findAllByProductsId(productId);
    }

    public Object updateStatusVariations(ResponseStatus responseStatus){
            Optional<ProductVariations> opProductVariation = repoProductVariations.findById(responseStatus.getId());
            if (!opProductVariation.isPresent()) {
                response.put("message", "không tìm thấy thông tín sản phẩm ");
                response.put("success", "false");
                return response;
            }
            ProductVariations old = opProductVariation.get();
            old.setIsActive(responseStatus.getStatus());
            ProductVariations data  = repoProductVariations.save(old);
            DtoProductVariations req = new DtoProductVariations();
            req.setActive(data.getIsActive());
            req.setId(data.getId());
            req.setBarcode(data.getBarcode());
            req.setImage(data.getImage());
            req.setPrice(data.getPrice());
            req.setQuantity(String.valueOf(data.getQuantity()));
            req.setProperties(data.getProperties());
            req.setSku(data.getSku());
            req.setWeight(data.getWeight());
            response.put("data", req);
            response.put("success","true");
            response.put("message", "cập nhật ẩn sản phẩm thành công");
            return response;
    }
}
