package com.vvlanding.repo;

import com.vvlanding.table.ViettelAddressProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoProvinceViettel extends JpaRepository<ViettelAddressProvince,Long> {
//    Optional<ViettelAddressProvince> findByProvinceName(String province);

    Optional<ViettelAddressProvince> findByProvinceName(String name);

}
