package com.vvlanding.repo;

import com.vvlanding.table.RefLandingPageUser;
import com.vvlanding.table.TitleLandingPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoTitleLandingPage extends JpaRepository<TitleLandingPage, Long> {
    List<TitleLandingPage> findAllByRefLandingPageUserId(Long refLandingPageUserId);

    Optional<TitleLandingPage> findById(Long id);

    List<TitleLandingPage> findByRefLandingPageUser(RefLandingPageUser refLandingPageUser);
}
