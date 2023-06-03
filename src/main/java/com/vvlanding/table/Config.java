package com.vvlanding.table;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "config_landing")
public class Config {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 60)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "phone")
    private String phone;

    @Column(name = "zalo", columnDefinition = "TEXT")
    private String zalo;

    @Column(name = "facebook", columnDefinition = "TEXT")
    private String facebook;

    @Column(name = "facebook_plug", columnDefinition = "TEXT")
    private String facebookPlug;

    @Column(name = "gg_analytics", columnDefinition = "TEXT")
    private String ggAnalytics;

    @Column(name = "plug_other", columnDefinition = "TEXT")
    private String plugOther;

    @Column(name = "ShopId")
    private Long ShopId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refLandingPageUser_id", referencedColumnName = "id")
    private RefLandingPageUser refLandingPageUser;

}