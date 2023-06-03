package com.vvlanding.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat_shopee")
public class ChatShopee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_type")
    private String messageType;

    @Column(name = "from_id")
    private int fromId;

    @Column(name = "from_shop_id")
    private int fromShopId;

    @Column(name = "to_id")
    private int toId;

    @Column(name = "to_shop_id")
    private int toShopId;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;

    @Column(name = "sender")
    private int sender;

    @Column(name = "created_date")
    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_detail_id")
    private ChatDetail chatDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_shopee_id")
    private ChannelShopee channelShopee;
}
