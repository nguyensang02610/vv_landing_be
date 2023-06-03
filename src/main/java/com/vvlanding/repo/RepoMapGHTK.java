package com.vvlanding.repo;

import com.vvlanding.table.MapGHTK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoMapGHTK extends JpaRepository<MapGHTK,Long> {
    Optional<MapGHTK> findByBillId(Long id);

    Optional<MapGHTK> findByGhtkOrderResponse_Label(String label);
}
