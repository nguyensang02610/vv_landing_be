package com.vvlanding.table;

import lombok.*;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bannerLanding")
public class BannerLanding {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "section")
    private String section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refLandingPageUser_id")
    private RefLandingPageUser refLandingPageUser;

}
