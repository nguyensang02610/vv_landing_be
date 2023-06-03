package com.vvlanding.repo;

import com.vvlanding.table.ShopInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoShopInfo extends JpaRepository<ShopInfo, Long> {

    List<ShopInfo> findByTitleContainingOrPhoneContaining(String title, String phone);

    List<ShopInfo> findAllBy(Pageable pageable);

    Optional<ShopInfo> findByShopToken(String shopToken);


}