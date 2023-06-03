package com.vvlanding.repo;

import com.vvlanding.table.StatusShipped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoStatus extends JpaRepository<StatusShipped, Long> {
    Optional<StatusShipped> findById(Long id);

    Optional<StatusShipped> findByStatusName(String name);

}
