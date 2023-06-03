package com.vvlanding.service;

import com.vvlanding.repo.RepoTitleLandingPage;
import com.vvlanding.table.RefLandingPageUser;
import com.vvlanding.table.TitleLandingPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SerTitleLandingPage {
    @Autowired
    RepoTitleLandingPage repoTitleLandingPage;

    public List<TitleLandingPage> findByRefLandingPageUserId(Long refLandingPageUserId) {
        return repoTitleLandingPage.findAllByRefLandingPageUserId(refLandingPageUserId);
    }

    public List<TitleLandingPage> findByRef(RefLandingPageUser refLandingPageUser) {
        return repoTitleLandingPage.findByRefLandingPageUser(refLandingPageUser);
    }
}
