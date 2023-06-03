package com.vvlanding.repo;

import com.vvlanding.table.Product;
import com.vvlanding.table.ProductVariations;
import com.vvlanding.table.ShopInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoProductVariations extends JpaRepository<ProductVariations, Long> {
    List<ProductVariations> findByProducts(Product product);

    List<ProductVariations> findAllBy(Pageable pageable);

    List<ProductVariations> findAllByProducts(Product product);

    Optional<ProductVariations> findBySkuContaining(String sku);

    List<ProductVariations> findAllByProductsId(Long productsId);

    List<ProductVariations> findALLById(Long id);

    Optional<ProductVariations> findById(Long id);

    Optional<ProductVariations> findByBarcode(String barCode);

    Optional<ProductVariations> findBySku(String sku);

    List<ProductVariations> findByProductsShopInfoAndProductsIsActive(ShopInfo shopInfo, boolean active);

    List<ProductVariations> findByProductsShopInfo(ShopInfo shopInfo);

}
