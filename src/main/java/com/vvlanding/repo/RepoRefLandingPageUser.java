package com.vvlanding.repo;

import com.vvlanding.table.Bill;
import com.vvlanding.table.RefLandingPageUser;
import com.vvlanding.table.ShopInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoRefLandingPageUser extends JpaRepository<RefLandingPageUser, Long> {
    List<RefLandingPageUser> findByDomain(String query);

    Optional<RefLandingPageUser> findByShopInfoIdAndId(Long shopId, Long id);

//    Optional<RefLandingPageUser> findBy(Long id, Long shopId);

    List<RefLandingPageUser> findByShopInfo(ShopInfo shopInfo);

    Optional<RefLandingPageUser> findById(Long id);

    List<RefLandingPageUser> findAllByShopInfo_IdAndDomainOrProductName(Long shopId, String domain, String productName);
}
