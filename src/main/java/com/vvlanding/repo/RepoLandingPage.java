package com.vvlanding.repo;

import com.vvlanding.table.LandingPage;
import com.vvlanding.table.TypeLd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoLandingPage extends JpaRepository<LandingPage, Long> {
    List<LandingPage> findAllBy(Pageable pageable);

    Optional<LandingPage> findById(Long id);

    List<LandingPage> findByTitleContaining(String title);

    Page<LandingPage> findByTitleContaining(Pageable pageable, String title);

    List<LandingPage> findAllByTypeLd(TypeLd typeLd);

    List<LandingPage> findByCodeLd(String codeld);

    List<LandingPage> findAllByTypeLdId(Long id);
}