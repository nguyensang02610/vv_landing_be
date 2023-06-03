package com.vvlanding.repo;

import com.vvlanding.table.OrderShopee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RepoOrderChannel extends JpaRepository<OrderShopee, Long> {
    Optional<OrderShopee> findByOrdersn(String ordersn);

    Optional<OrderShopee> findByBillId(Long billId);

    @Query("select a from OrderShopee a where a.channelShopee.shopeeShopId =:shopeeId AND a.buyerUserId=:userId AND a.orderStatus=:query")
    List<OrderShopee> findByChannelShopeeShopeeShopIdAndBuyerUserIdAndQuery(Long shopeeId,String userId, String query);

    @Query("select a from OrderShopee a where a.channelShopee.shopeeShopId =:shopeeId AND a.buyerUserId=:userId AND a.orderStatus=:query")
    List<OrderShopee> findByChannelShopeeShopeeShopIdAndBuyerUserIdAndQueryPage(Long shopeeId,String userId, String query, Pageable pageable);

    @Query("select a from OrderShopee a where a.channelShopee.shopeeShopId =:shopeeId AND a.buyerUserId=:userId")
    List<OrderShopee> findByChannelShopeeShopeeShopIdAndBuyerUserId(Long shopeeId,String userId);

    @Query("select a from OrderShopee a where a.channelShopee.shopeeShopId =:shopeeId AND a.buyerUserId=:userId")
    List<OrderShopee> findByChannelShopeeShopeeShopIdAndBuyerUserIdPage(Long shopeeId,String userId, Pageable pageable);
}
