package com.vvlanding.repo;

import com.vvlanding.table.ShopUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoShopUserRole extends JpaRepository<ShopUserRole,Long> {
    Optional<ShopUserRole> findByShopInfoIdAndUserUsername(Long id,String name);

    Optional<ShopUserRole> findByShopInfoIdAndUserId(Long shopId,Long userId);

    List<ShopUserRole> findByUserId(Long id);

    List<ShopUserRole> findByShopInfoId(Long shopId);

    Optional<ShopUserRole> findByShopInfoIdAndUserPhone(Long id,String phone);
}
