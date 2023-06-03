package com.vvlanding.shopee.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetConversationList {
    private String direction;
    private String type;
    private int next_timestamp_nano;
    private int page_size;
}
