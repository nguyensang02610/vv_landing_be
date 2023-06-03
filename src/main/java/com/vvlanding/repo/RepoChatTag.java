package com.vvlanding.repo;

import com.vvlanding.table.ChatTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoChatTag extends JpaRepository<ChatTag,Long> {

    List<ChatTag> findByShopInfoId(Long id);

    Optional<ChatTag> findByShopInfoIdAndKeyAndValue(Long shopId,String key,String value);

    Optional<ChatTag> findByShopInfoIdAndKey(Long shopId,String key);

    Optional<ChatTag> findByShopInfoIdAndValue(Long shopId,String value);
}
