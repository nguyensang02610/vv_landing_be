package com.vvlanding.repo;

import com.vvlanding.table.RefLandingPageUser;
import com.vvlanding.table.TitleLanding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoTitleLanding extends JpaRepository<TitleLanding, Long> {
    List<TitleLanding> findAllByRefLandingPageUser1Id(Long refLandingPageUser1Id);

    Optional<TitleLanding> findById(Long id);

    List<TitleLanding> findByRefLandingPageUser1(RefLandingPageUser refLandingPageUser);
}
