package com.vvlanding.repo;

import com.vvlanding.table.ShopGHN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoShopGhn extends JpaRepository<ShopGHN, Long> {
    Optional<ShopGHN> findByShopGhnId(Long shopId);

    List<ShopGHN> findAllByShopInfoId(Long id);
}
