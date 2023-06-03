package com.vvlanding.repo;

import com.vvlanding.table.ViettelAddressDistrict;
import com.vvlanding.table.ViettelAddressProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoDistrictViettel extends JpaRepository<ViettelAddressDistrict,Long> {

    @Query(value = "SELECT * FROM viettel_district a WHERE a.district_name like %:name% ", nativeQuery = true)
    Optional<ViettelAddressDistrict> findByDistrictNames(String name);

    Optional<ViettelAddressDistrict> findByDistrictNameAndProvinceId(String name , Integer provinceId);

}