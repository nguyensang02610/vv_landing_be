package com.vvlanding.repo;

import com.vvlanding.table.RefChatTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoRefChatTag extends JpaRepository<RefChatTag,Long> {

    List<RefChatTag> findByChatTagId(Long id);

    List<RefChatTag> findByChatDetail_ConversationId(String id);
}
