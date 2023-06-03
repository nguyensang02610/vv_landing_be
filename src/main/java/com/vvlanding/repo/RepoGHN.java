package com.vvlanding.repo;

import com.vvlanding.table.GHNOrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoGHN extends JpaRepository<GHNOrderResponse, Long> {
    Optional<GHNOrderResponse> findByOrderCode(String orderCode);
}
