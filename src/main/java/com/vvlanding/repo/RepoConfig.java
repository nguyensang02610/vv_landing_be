package com.vvlanding.repo;

import com.vvlanding.table.Config;
import com.vvlanding.table.RefLandingPageUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoConfig extends JpaRepository<Config, Long> {
    List<Config> findAllBy(Pageable pageable);

    Optional<Config> findById(Long id);

    List<Config> findByRefLandingPageUser(RefLandingPageUser refLandingPageUser);

    Optional<Config> findByIdAndRefLandingPageUser_id(Long id, Long refID);
}
