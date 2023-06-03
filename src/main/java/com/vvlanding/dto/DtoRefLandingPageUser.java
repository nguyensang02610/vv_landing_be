package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoRefLandingPageUser {
    private Long id;
    private Long productId;
    private String landingPageName;
    private String landingBanner;
    private String userName;
    private String productName;
    private Long userId;
    private Long shopId;
    private Long landingId;
    private Boolean status;
    private String domain;
    private String codeLd;
    private List<DtoBannerLanding> dtoBannerLandings = new ArrayList<>();
    private List<DtoTitleLanding> dtoTitleLandings = new ArrayList<>();
    private List<DtoTitleLandingPage> dtoTitleLandingPages = new ArrayList<>();

    public DtoRefLandingPageUser(long id, String userName, String productName, Boolean status, String domain, long productId, long userId, String landingPageName, String landingBanner, long landingId, long shopId, String codeLd, List<DtoTitleLanding> dtoTitleLandings, List<DtoBannerLanding> dtoBannerLandings, List<DtoTitleLandingPage> dtoTitleLandingPages) {
        this.id = id;
        this.landingPageName = landingPageName;
        this.userName = userName;
        this.productName = productName;
        this.status = status;
        this.domain = domain;
        this.productId = productId;
        this.userId = userId;
        this.landingId = landingId;
        this.shopId = shopId;
        this.codeLd = codeLd;
        this.landingBanner = landingBanner;
        this.dtoBannerLandings = dtoBannerLandings;
        this.dtoTitleLandings = dtoTitleLandings;
        this.dtoTitleLandingPages = dtoTitleLandingPages;
    }

    public DtoRefLandingPageUser(long idRef, List<DtoBannerLanding> dtoBannerLandings, long shopId) {
        this.id = idRef;
        this.dtoBannerLandings = dtoBannerLandings;
        this.shopId = shopId;
    }
}
