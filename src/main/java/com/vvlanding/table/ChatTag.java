package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_tag")
public class ChatTag {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyName")
    private String key;

    @Column(name = "valueName")
    private String value;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatTag")
    @JsonIgnore
    private List<RefChatTag> refChatTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopInfo_id")
    @JsonIgnore
    private ShopInfo shopInfo;
}
