package com.vvlanding.repo;

import com.vvlanding.table.ChannelShopee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoChannelShopee extends JpaRepository<ChannelShopee, Long> {

    List<ChannelShopee> findAllByShopeeShopName(String name);

    List<ChannelShopee> findAllByShopId(Long shopId);

    List<ChannelShopee> findAllByShopId(Long shopId, Pageable pageable);

    Optional<ChannelShopee> findByShopeeShopId(Long shopeeShopID);

    Optional<ChannelShopee> findByShopeeShopIdAndShopId(Long shopeeId,Long shopId);
}
