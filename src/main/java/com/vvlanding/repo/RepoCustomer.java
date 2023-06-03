package com.vvlanding.repo;

import com.vvlanding.table.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepoCustomer extends JpaRepository<Customer, Long> {
    List<Customer> findByPhoneAndShopId(String phone,Long shopId);
    Optional<Customer> findById(Long id);
    List<Customer> findAllByShopId(Long shopId);
    List<Customer> findAllByShopId(Long shopId, Pageable pageable);
    List<Customer> findByShopIdAndPhoneOrTitle(Long shopId, String phone, String title);
    List<Customer> findAllBy(Pageable pageable);
    Optional<Customer> findByIdAndShopId(Long id, Long shopId);
    @Query(value = "SELECT * FROM sub_info a ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Customer> findAllOrderByTitleDesc();
}