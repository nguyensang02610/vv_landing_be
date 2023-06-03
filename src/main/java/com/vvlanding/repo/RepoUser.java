package com.vvlanding.repo;

import com.vvlanding.table.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoUser extends JpaRepository<User, Long> {
    List<User> findAllBy(Pageable pageable);

    Optional<User> findByIdAndUsername(Long userId, String username);

    Optional<User> findByUsername(String username);

    List<User> findByPhone(String phone);
}