package com.vvlanding.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class DtoChatShopee {

    private Long id;

    private String messageType;

    private int fromId;

    private int fromShopId;

    private int toId;

    private int toShopId;

    private String conversationId;

    private String messageId;

    private String content;

    private int sender;

    private Date createDate;
}
