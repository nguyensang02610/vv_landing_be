package com.vvlanding.repo;

import com.vvlanding.table.ViettelResponseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoViettel extends JpaRepository<ViettelResponseOrder,Long> {
}
