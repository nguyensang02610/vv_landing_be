package com.vvlanding.repo;

import com.vvlanding.table.GHTKOrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoGHTK extends JpaRepository<GHTKOrderResponse, Long> {
}
