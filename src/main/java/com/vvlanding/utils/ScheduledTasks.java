package com.vvlanding.utils;

import com.vvlanding.repo.RepoRefLandingPageUser;
import com.vvlanding.service.SerRefLandingPageUser;
import com.vvlanding.table.RefLandingPageUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    RepoRefLandingPageUser repoRefLandingPageUser;
//3600000
    @Scheduled(fixedRate = 3600000)
    public void scheduleTaskWithFixedRate() {
        List<RefLandingPageUser> list = repoRefLandingPageUser.findAll();
        for (RefLandingPageUser r: list) {
            if (r.getEndTime()!= null){
                if (r.getEndTime().getTime() < new Date().getTime()){
                    r.setStatus(false);
                    repoRefLandingPageUser.save(r);
                }
            }
        }
    }

    public void scheduleTaskWithFixedDelay() {
    }

    public void scheduleTaskWithInitialDelay() {
    }

    public void scheduleTaskWithCronExpression() {
    }
}
