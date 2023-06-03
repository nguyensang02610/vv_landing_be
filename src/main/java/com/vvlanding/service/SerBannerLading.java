package com.vvlanding.service;

import com.vvlanding.dto.DtoBannerLanding;
import com.vvlanding.dto.DtoRefLandingPageUser;
import com.vvlanding.repo.RepoBannerLanding;
import com.vvlanding.repo.RepoRefLandingPageUser;
import com.vvlanding.table.BannerLanding;
import com.vvlanding.table.RefLandingPageUser;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SerBannerLading {
    @Autowired
    RepoBannerLanding repoBannerLanding;

    @Autowired
    RepoRefLandingPageUser repoRefLandingPageUser;

    public List<BannerLanding> findByByRefLandingPageUserId(Long refLandingPageUserId) {
        return repoBannerLanding.findAllByRefLandingPageUserId(refLandingPageUserId);
    }

    public List<BannerLanding> findByRef(RefLandingPageUser refLandingPageUser) {
        return repoBannerLanding.findByRefLandingPageUser(refLandingPageUser);
    }

    public Optional<RefLandingPageUser> find(long id, ShopInfo shopInfo) {
        return repoRefLandingPageUser.findById(id);
    }

    public static DtoRefLandingPageUser GetAllBannerByRefId(RefLandingPageUser refLandingPageUser, List<BannerLanding> bannerLandings, ShopInfo shopInfo) {
        long idRef = refLandingPageUser.getId();
        long shopId = shopInfo.getId();
        List<DtoBannerLanding> dtoBannerLandings = new ArrayList<DtoBannerLanding>();
        for (BannerLanding bannerLanding : bannerLandings) {
            long id = bannerLanding.getId();
            String title = bannerLanding.getTitle();
            String section = bannerLanding.getSection();
            String image = bannerLanding.getImage();
            DtoBannerLanding dtoBannerLanding = new DtoBannerLanding(id, title, image, section);
            dtoBannerLandings.add(dtoBannerLanding);
        }
        DtoRefLandingPageUser doNewRefLandingPageUser = new DtoRefLandingPageUser(idRef, dtoBannerLandings, shopId);
        return doNewRefLandingPageUser;
    }

    public void deleteBanner(long id, ShopInfo shopInfo) {
        Optional<RefLandingPageUser> refLandingPageUser = find(id, shopInfo);
        if (refLandingPageUser.isPresent()) {
            RefLandingPageUser refLandingPageUser1 = refLandingPageUser.get();
            List<BannerLanding> bannerLanding1 = refLandingPageUser1.getBannerLandings();
            repoBannerLanding.deleteAll(bannerLanding1);
        }
    }


}
