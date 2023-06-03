package com.vvlanding.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vvlanding.table.AddressDistrict;

public interface DistrictRepository extends JpaRepository<AddressDistrict, String> {

    Optional<AddressDistrict> findByNameIgnoreCase(String district);

    List<AddressDistrict> findByName(String name);

}
