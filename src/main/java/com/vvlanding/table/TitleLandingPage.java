package com.vvlanding.table;

import lombok.*;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "titleLandingPage")
public class TitleLandingPage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "section")
    private int section;

    @Column(name = "sub_section")
    private int sub_section;

    @Column(name = "kind")
    private int kind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refLandingPageUser_id")
    private RefLandingPageUser refLandingPageUser;
}
