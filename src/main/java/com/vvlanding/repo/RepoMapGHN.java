package com.vvlanding.repo;

import com.vvlanding.table.MapGHN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoMapGHN extends JpaRepository<MapGHN, Long> {
    Optional<MapGHN> findByBillId(Long id);

    Optional<MapGHN> findByGhnOrderResponse_OrderCode(String orderCode);
}
