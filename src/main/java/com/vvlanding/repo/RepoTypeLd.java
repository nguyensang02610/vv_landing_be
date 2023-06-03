package com.vvlanding.repo;

import com.vvlanding.table.Config;
import com.vvlanding.table.TypeLd;
import com.vvlanding.table.Units;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoTypeLd extends JpaRepository<TypeLd, Long> {
    List<TypeLd> findAll();

    List<TypeLd> findAllBy(Pageable pageable);

    Optional<TypeLd> findById(Long id);

    Page<TypeLd> findByTitle(String title, Pageable pageable);

    List<TypeLd> findByTitleContaining(String title);

    List<TypeLd> findByTitle(String title);
}