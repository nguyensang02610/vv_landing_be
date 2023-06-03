package com.vvlanding.repo;

import com.vvlanding.table.MapViettel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoMapViettel extends JpaRepository<MapViettel,Long> {
    Optional<MapViettel> findByBillId(Long id);

    Optional<MapViettel> findByViettelResponseOrder_OrderNumber(String orderNumber);
}
