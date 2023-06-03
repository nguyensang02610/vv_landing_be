package com.vvlanding.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vvlanding.table.AddressProvince;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<AddressProvince, String> {

    Optional<AddressProvince> findByNameIgnoreCase(String province);

    Optional<AddressProvince> findByName(String province);
}
