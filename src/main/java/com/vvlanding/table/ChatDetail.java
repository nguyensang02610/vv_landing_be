package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat_detail")
public class ChatDetail {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "to_id")
    private int toId;

    @Column(name = "to_name")
    private String toName;

    @Column(name = "to_avatar")
    private String to_avatar;

    @Column(name = "shop_id")
    private int shopId;

    @Column(name = "pinned")
    private boolean pinned;
    
    @Column(name = "on_view")
    private Boolean onView;

    @Column(name = "message_type")
    private String message_type;

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;

    @Column(name = "last_message_timestamp")
    private Date lastMessageTimestamp;

    @Column(name = "shop_info_id")
    private Long shopInfoId;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "chatDetail")
    private List<RefChatTag> chatTags;
}
