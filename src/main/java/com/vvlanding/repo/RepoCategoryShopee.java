package com.vvlanding.repo;

import com.vvlanding.table.CategoryShopee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoCategoryShopee extends JpaRepository<CategoryShopee,Long> {
    List<CategoryShopee> findByDisplayCategoryNameContaining(String name);
}
