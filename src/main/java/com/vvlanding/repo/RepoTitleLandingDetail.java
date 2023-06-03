package com.vvlanding.repo;

import com.vvlanding.table.Properties;
import com.vvlanding.table.TitleLanding;
import com.vvlanding.table.TitleLandingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoTitleLandingDetail extends JpaRepository<TitleLandingDetail, Long> {
    List<TitleLandingDetail> findAllByTitleAndAndContentAndImage(String title, String content, String image);
}
