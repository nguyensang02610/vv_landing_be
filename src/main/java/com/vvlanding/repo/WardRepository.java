package com.vvlanding.repo;

import java.util.List;
import java.util.Optional;

import com.vvlanding.table.AddressProvince;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vvlanding.table.AddressDistrict;
import com.vvlanding.table.AddressWard;

public interface WardRepository extends JpaRepository<AddressWard, String> {
    Optional<AddressWard> findByNameIgnoreCase(String ward);

    List<AddressWard> findByName(String ward);

}
