package com.vvlanding.repo;

import com.vvlanding.table.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoProperties extends JpaRepository<Properties, Long> {
    List<Properties> findByKeynameContainingOrValueContaining(String keyname, String value);

    List<Properties> findAllBy(Pageable pageable);

    List<Properties> findAllByProductVariations(ProductVariations productVariations);

    List<Properties> findAllByKeynameAndValue(String Key, String value);
}
