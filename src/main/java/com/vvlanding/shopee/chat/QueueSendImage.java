package com.vvlanding.shopee.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueSendImage {
    private Long shopeeId;
    private String partnerKey;
    private Long partnerID;
    private String conversation_id;
    private Long shopId;
    private String file;
}
