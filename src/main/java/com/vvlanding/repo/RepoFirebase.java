package com.vvlanding.repo;

import com.vvlanding.table.Firebase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoFirebase extends JpaRepository<Firebase,Long> {

    Optional<Firebase> findByUserIdAndShopInfoId(Long userId,Long shopId);

    Optional<Firebase> findByShopInfoId(Long shopId);
}