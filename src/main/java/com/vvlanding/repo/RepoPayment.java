package com.vvlanding.repo;

import com.vvlanding.table.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoPayment extends JpaRepository<Payment,Long> {
    List<Payment> findByShopInfoId(Long id);
    Optional<Payment> findByShopInfoIdAndName(Long id,String name);
}
