package com.vvlanding.shopee.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)

public class SendValue {
    private String text;
    private String url;
    private String order;
    private Long item;
    private  String conversation_id;
    private  long shopeeId;
    private String partnerKeyV2;
    private long partnerIdV2;
    private Long ShopId;

}
