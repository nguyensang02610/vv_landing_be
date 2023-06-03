package com.vvlanding.shopee.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversations {
    private String conversation_id;
    private int to_id;
    private String to_name;
    private String to_avatar;
    private int shop_id;
    private int unread_count;
    private boolean pinned;
    private String latest_message_type;
    private Long last_message_timestamp;
    private MessageContent latest_message_content;
}
