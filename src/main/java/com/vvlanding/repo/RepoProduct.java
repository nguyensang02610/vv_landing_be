package com.vvlanding.repo;

import com.vvlanding.table.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoProduct extends JpaRepository<Product, Long> {


    List<Product> findAllByShopInfoIdAndIsActive(Long shopId, boolean active);


    List<Product> findAllByShopInfoIdAndIsActive(Long shopId, boolean active, Pageable pageable);

    List<Product> findAllBy(Pageable pageable);

    Optional<Product> findById(Long id);

    Optional<Product> findAllByIdAndShopInfoShopToken(Long id, String shoptoken);

    Optional<Product> findBySkuAndShopInfo_Id(String query, Long id);

    List<Product> findBySku(String query);

    Optional<Product> findByIdAndShopInfo_Id(Long id, Long shopId);

    List<Product> findAllByShopInfo_IdAndTitleOrSku(Long shopId, String title, String sku);

    List<Product> findAllByShopInfo_IdAndTitleOrSku(Long shopId, String title, String sku, Pageable pageable);


}