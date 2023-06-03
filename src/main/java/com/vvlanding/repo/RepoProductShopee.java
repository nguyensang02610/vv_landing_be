package com.vvlanding.repo;

import com.vvlanding.table.ProductShopee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoProductShopee extends JpaRepository<ProductShopee,Long> {

    Optional<ProductShopee> findByItemIdAndShopId(Long itemId,Long shopId);

    List<ProductShopee> findByShopeeIdAndShopId(Long shopeeId,Long shopId);

    ProductShopee findByProductId(Long id);
}
