package com.vvlanding.repo;

import com.vvlanding.table.Units;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoUnit extends JpaRepository<Units, Long> {
    List<Units> findAllBy(Pageable pageable);

    Optional<Units> findById(Long id);

    Optional<Units> findByNameContaining(String name);

    List<Units> findByName(String name);

}
