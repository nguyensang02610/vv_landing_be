package com.vvlanding.repo;

import com.vvlanding.table.ChatDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoChatDetail extends JpaRepository<ChatDetail,Long> {

    Optional<ChatDetail> findByConversationId(String conversation);

    Optional<ChatDetail> findByConversationIdAndShopInfoId(String conversation, Long shopId);

    List<ChatDetail> findAllByShopInfoIdOrderByLastMessageTimestampDesc(Long shopId);

    Page<ChatDetail> findAllByShopInfoIdOrderByLastMessageTimestampDesc(Long shopId, Pageable pageable);

    List<ChatDetail> findByShopIdAndToNameContaining(int shopeeId,String to_name);

    List<ChatDetail> findByShopInfoIdAndToNameContaining( Long shopInfoId,String to_name);

    List<ChatDetail> findByShopInfoIdAndOnView( Long shopInfoId, Boolean view);
}
