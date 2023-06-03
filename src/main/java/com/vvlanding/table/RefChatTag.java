package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ref_chat_tag")
public class RefChatTag {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_tag_id")
    private ChatTag chatTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_detail_id")
    @JsonIgnore
    private ChatDetail chatDetail;
}
