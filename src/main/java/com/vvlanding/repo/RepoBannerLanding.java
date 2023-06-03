package com.vvlanding.repo;

import com.vvlanding.table.BannerLanding;
import com.vvlanding.table.RefLandingPageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoBannerLanding extends JpaRepository<BannerLanding, Long> {
    List<BannerLanding> findAllByRefLandingPageUserId(Long refLandingPageUserId);

    Optional<BannerLanding> findById(Long id);

    List<BannerLanding> findByRefLandingPageUser(RefLandingPageUser refLandingPageUser);

    //    List<BannerLanding> findAllByRefLandingPageUser(RefLandingPageUser refLandingPageUser);
    Optional<BannerLanding> findByIdAndRefLandingPageUser_id(Long id, Long refID);
}
