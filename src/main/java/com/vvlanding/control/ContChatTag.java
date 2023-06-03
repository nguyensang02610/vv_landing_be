package com.vvlanding.control;

import com.vvlanding.shopee.chat.ChatTagDTO;
import com.vvlanding.shopee.service.shopee.SerChatTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/chat/tag")
public class ContChatTag {

    @Autowired
    SerChatTag serChatTag;

    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public ResponseEntity<?> insert(@RequestBody ChatTagDTO chatTagDTO){
        return serChatTag.insert(chatTagDTO);
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseEntity<?> update(@RequestBody ChatTagDTO chatTagDTO){
        return serChatTag.update(chatTagDTO);
    }
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseEntity<?> delete(@RequestParam Long id){
        return serChatTag.delete(id);
    }
    @RequestMapping(value = "/get/conversation",method = RequestMethod.GET)
    public ResponseEntity<?> getConversation(@RequestParam String conversationId){
        return serChatTag.getByConversation(conversationId);
    }
    @RequestMapping(value = "/get/shop",method = RequestMethod.GET)
    public ResponseEntity<?> getByShop(@RequestParam Long shopId){
        return serChatTag.getByShop(shopId);
    }

    @RequestMapping(value = "/add/tag/conversation",method = RequestMethod.POST)
    public ResponseEntity<?> addConversation(@RequestParam List<Long> tagId, @RequestParam String conversationId){
        return serChatTag.addConversationTag(conversationId,tagId);
    }
    @RequestMapping(value = "/delete/conversationId",method = RequestMethod.POST)
    public ResponseEntity<?> deleteByConversationId(@RequestParam String conversationId){
        return serChatTag.deleteRefChatTag(conversationId);
    }
    @RequestMapping(value = "/delete/conversation/ref_tag_id",method = RequestMethod.POST)
    public ResponseEntity<?> deleteByConversationId(@RequestParam Long refTagId){
        return serChatTag.deleteRefChatTagById(refTagId);
    }
}
