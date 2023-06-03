package com.vvlanding.repo;

import com.vvlanding.table.ChatShopee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoChatShopee extends JpaRepository<ChatShopee,Long> {

    Optional<ChatShopee> findByMessageId(String messageId);

    List<ChatShopee> findByConversationIdAndContentContaining(String conver,String content);

    List<ChatShopee> findAllByConversationIdAndChannelShopeeIdOrderByIdDesc(String ConversationId, Long channelShopeeId);

    Page<ChatShopee> findAllByConversationIdAndChannelShopeeIdOrderByCreateDateDesc(String ConversationId,Long channelShopeeId, Pageable pageable);

}
