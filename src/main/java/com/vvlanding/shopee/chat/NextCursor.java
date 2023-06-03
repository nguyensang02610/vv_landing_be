package com.vvlanding.shopee.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NextCursor {
    private Long next_message_time_nano;
    private String conversation_id;
}
