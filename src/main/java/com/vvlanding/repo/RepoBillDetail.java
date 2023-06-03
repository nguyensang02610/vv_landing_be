package com.vvlanding.repo;

import com.vvlanding.table.Bill;
import com.vvlanding.table.BillDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoBillDetail extends JpaRepository<BillDetail, Long> {
    List<BillDetail> findAllBy(Pageable pageable);

    Optional<BillDetail> findById(Long id);


}