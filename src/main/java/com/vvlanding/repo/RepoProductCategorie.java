package com.vvlanding.repo;

import com.vvlanding.table.ProductCategories;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoProductCategorie extends JpaRepository<ProductCategories, Long> {
    List<ProductCategories> findAllBy(Pageable pageable);

    Optional<ProductCategories> findById(Long id);

    Optional<ProductCategories> findByTitle(String title);
}
