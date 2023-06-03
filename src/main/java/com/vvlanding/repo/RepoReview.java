package com.vvlanding.repo;

import com.vvlanding.table.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoReview extends JpaRepository<Review, Long> {
    List<Review> findAllByShopInfoId(Long shopId);

    Optional<Review> findByIdAndAndShopInfoId(Long id, Long shopId);
}
