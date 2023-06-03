package com.vvlanding.shopee.chat;

import com.vvlanding.table.ChatDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ChatTagDTO {
    private Long id;

    private String key;

    private String value;

    private Long shopId;

//    private String conversationId;

//    private List<ChatDetail> chatDetails;
}
