package com.vvlanding.shopee.service.shopee;

import com.vvlanding.repo.RepoChatDetail;
import com.vvlanding.repo.RepoChatTag;
import com.vvlanding.repo.RepoRefChatTag;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.shopee.chat.ChatTagDTO;
import com.vvlanding.table.ChatDetail;
import com.vvlanding.table.ChatTag;
import com.vvlanding.table.RefChatTag;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SerChatTag {

    @Autowired
    RepoChatTag repoChatTag;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoChatDetail repoChatDetail;

    @Autowired
    RepoRefChatTag repoRefChatTag;

    public ResponseEntity<?> insert(ChatTagDTO chatTagDTO){
        try {
            Optional<ShopInfo> shopInfo = repoShopInfo.findById(chatTagDTO.getShopId());
            if (!shopInfo.isPresent()) return ResponseEntity.ok(Constant.res("không tìm thấy shop",false,null));
            Optional<ChatTag> chatTagOptional = repoChatTag.findByShopInfoIdAndKey(chatTagDTO.getShopId(),chatTagDTO.getKey());
            if (chatTagOptional.isPresent()) return ResponseEntity.ok(Constant.res("tag đã tồn tại",false,null));
            Optional<ChatTag> chatTagOptional1 = repoChatTag.findByShopInfoIdAndValue(chatTagDTO.getShopId(),chatTagDTO.getValue());
            if (chatTagOptional1.isPresent()) return ResponseEntity.ok(Constant.res("tag đã tồn tại",false,null));
            ChatTag chatTag = new ChatTag();
            chatTag.setKey(chatTagDTO.getKey());
            chatTag.setValue(chatTagDTO.getValue());
            chatTag.setShopInfo(shopInfo.get());
            repoChatTag.save(chatTag);
            return ResponseEntity.ok(Constant.res("ok",true,null));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
    public ResponseEntity<?> update(ChatTagDTO chatTagDTO){
        try {
            Optional<ChatTag> chatTag = repoChatTag.findById(chatTagDTO.getId());
            if (!chatTag.isPresent()) return ResponseEntity.ok(Constant.res("Không tìm thấy tag",false,null));
            ChatTag chatTags = chatTag.get();
            chatTags.setKey(chatTagDTO.getKey());
            chatTags.setValue(chatTagDTO.getValue());
            repoChatTag.save(chatTags);
            return ResponseEntity.ok(Constant.res("ok",true,null));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
    public ResponseEntity<?> delete(Long id){
        try {
            Optional<ChatTag> chatTag = repoChatTag.findById(id);
            if (!chatTag.isPresent()) return ResponseEntity.ok(Constant.res("không tìm thấy tag",false,null));
            List<RefChatTag> chatTags = repoRefChatTag.findByChatTagId(id);
            if (chatTags.size() > 0) repoRefChatTag.deleteAll(chatTags);
            repoChatTag.delete(chatTag.get());
            return ResponseEntity.ok(Constant.res("ok",true,null));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
    public ResponseEntity<?> deleteRefChatTag(String conversationId){
        try {
            List<RefChatTag> refChatTags = repoRefChatTag.findByChatDetail_ConversationId(conversationId);
            if (refChatTags.size() > 0){
                repoRefChatTag.deleteAll(refChatTags);
            }
            return ResponseEntity.ok(Constant.res("ok",true,null));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
    public ResponseEntity<?> deleteRefChatTagById(Long id){
        try {
            repoRefChatTag.deleteById(id);
            return ResponseEntity.ok(Constant.res("ok",true,null));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
    public ResponseEntity<?> addConversationTag(String conversationId,List<Long> tagId){
        try {
            Optional<ChatDetail> chatDetail = repoChatDetail.findByConversationId(conversationId);
            if (!chatDetail.isPresent()) return ResponseEntity.ok(Constant.res("Không tìm thấy conversation",false,null));
            for (Long i:tagId) {
                Optional<ChatTag> chatTag = repoChatTag.findById(i);
                if (!chatTag.isPresent()) return ResponseEntity.ok(Constant.res("Không tìm thấy tag",false,null));
                RefChatTag refChatTag = new RefChatTag();
                refChatTag.setChatTag(chatTag.get());
                refChatTag.setChatDetail(chatDetail.get());
                repoRefChatTag.save(refChatTag);
            }
            return ResponseEntity.ok(Constant.res("ok",true,null));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
    public ResponseEntity<?> getByConversation(String conversationId){
        try {
            List<RefChatTag> chatTags = repoRefChatTag.findByChatDetail_ConversationId(conversationId);
            List<ChatTagDTO> chatTagDTOS = new ArrayList<>();
            for (RefChatTag c: chatTags) {
                ChatTagDTO chatTagDTO = new ChatTagDTO();
                chatTagDTO.setId(c.getId());
                chatTagDTO.setKey(c.getChatTag().getKey());
                chatTagDTO.setValue(c.getChatTag().getValue());
                chatTagDTOS.add(chatTagDTO);
            }
            return ResponseEntity.ok(Constant.res("ok",true,chatTagDTOS));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
    public ResponseEntity<?> getByShop(Long shopId){
        try {
            List<ChatTag> chatTags = repoChatTag.findByShopInfoId(shopId);
            List<ChatTagDTO> chatTagDTOS = new ArrayList<>();
            for (ChatTag c: chatTags) {
                ChatTagDTO chatTagDTO = new ChatTagDTO();
                chatTagDTO.setId(c.getId());
                chatTagDTO.setKey(c.getKey());
                chatTagDTO.setValue(c.getValue());
                chatTagDTO.setShopId(c.getShopInfo().getId());
                chatTagDTOS.add(chatTagDTO);
            }
            return ResponseEntity.ok(Constant.res("ok",true,chatTagDTOS));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
}
