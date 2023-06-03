package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ref_landing_page_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "domain"
        })
})
public class RefLandingPageUser {
    @Id
    @Column(name = "id", nullable = false, unique = false, length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "domain")
    private String domain;

    @Column(name = "startTime")
    private Date startTime;

    @Column(name = "endTime")
    private Date endTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "landingpage_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LandingPage landingPage;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shop_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShopInfo shopInfo;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refLandingPageUser")
    private List<BannerLanding> bannerLandings;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refLandingPageUser1")
    private Set<TitleLanding> titleLandings;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refLandingPageUser")
    private Set<TitleLandingPage> titleLandingPages;

    @JsonIgnore
    @OneToOne(mappedBy = "refLandingPageUser")
    private Config config;

}
