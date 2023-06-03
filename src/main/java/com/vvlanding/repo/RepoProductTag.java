package com.vvlanding.repo;

import com.vvlanding.table.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoProductTag extends JpaRepository<ProductTag, Long> {
    List<ProductTag> findByTitle(String title);

}
