package com.vvlanding.repo;

import com.vvlanding.table.ShopeeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoShopeeToken extends JpaRepository<ShopeeToken,Long> {

    Optional<ShopeeToken> findByShopeeIdAndAppId(Long shopeeId,Long appId);

    Optional<ShopeeToken> findByMerchantId(Long merchant);
}
