package com.vvlanding.service;

import com.vvlanding.repo.RepoTitleLanding;
import com.vvlanding.table.RefLandingPageUser;
import com.vvlanding.table.TitleLanding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerTitleLading {
    @Autowired
    RepoTitleLanding repoTitleLanding;

    public List<TitleLanding> findByRefLandingPageUser1Id(Long refLandingPageUser1Id) {
        return repoTitleLanding.findAllByRefLandingPageUser1Id(refLandingPageUser1Id);
    }

    public List<TitleLanding> findByRef(RefLandingPageUser refLandingPageUser) {
        return repoTitleLanding.findByRefLandingPageUser1(refLandingPageUser);
    }
}
