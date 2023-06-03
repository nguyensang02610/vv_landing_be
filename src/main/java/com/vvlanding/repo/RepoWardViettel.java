package com.vvlanding.repo;

import com.vvlanding.table.ViettelAddressWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoWardViettel extends JpaRepository<ViettelAddressWard,Long> {
}
