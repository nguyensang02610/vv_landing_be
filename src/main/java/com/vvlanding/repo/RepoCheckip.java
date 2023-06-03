package com.vvlanding.repo;

import com.vvlanding.table.Bill;
import com.vvlanding.table.Checkip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepoCheckip extends JpaRepository<Checkip, Long> {

    List<Checkip> findAll();

    List<Checkip> findAllBy(Pageable pageable);

    Optional<Checkip> findById(Long id);

    Optional<Checkip> findByIpaddress(String ipaddress);

    @Query(value = "SELECT * FROM checkip c WHERE c.ipaddress =:checkId ORDER BY c.id DESC LIMIT 15 ",nativeQuery = true)
    List<Checkip> findCheckIp(String checkId);

    List<Checkip> findAllByIpaddress(String idAddress);
}
