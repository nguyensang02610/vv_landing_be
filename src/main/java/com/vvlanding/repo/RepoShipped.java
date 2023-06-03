package com.vvlanding.repo;

import com.vvlanding.table.Shipped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RepoShipped extends JpaRepository<Shipped,Long> {

    Optional<Shipped> findByToken(String token);

    Optional<Shipped> findByNameAndShopInfoId(String name,Long shopId);

    List<Shipped> findAllByShopInfoId(Long shopId);
}
