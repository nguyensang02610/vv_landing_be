package com.vvlanding.shopee.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Long id;

    private String messageType;

    private String conversationId;

    private String messageId;

    private String content;

    private int sender;

    private Date createDate;
}
