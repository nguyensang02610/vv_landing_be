package com.vvlanding.service;

import com.vvlanding.repo.RepoFirebase;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.repo.RepoShopUserRole;
import com.vvlanding.table.Firebase;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.table.ShopUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerFirebase {

    @Autowired
    RepoFirebase repoFirebase;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoShopUserRole repoShopUserRole;

    public void save(String token, Long userId) {
        List<ShopUserRole> shopUserRole = repoShopUserRole.findByUserId(userId);
        if (shopUserRole.size() > 0){
            ShopInfo shopInfo = shopUserRole.get(0).getShopInfo();
            Optional<Firebase> firebase = repoFirebase.findByUserIdAndShopInfoId(userId, shopInfo.getId());
            if (firebase.isPresent()) {
                Firebase fire = firebase.get();
                fire.setToken(token);
                repoFirebase.save(fire);
            } else {
                Firebase fire = new Firebase();
                fire.setToken(token);
                fire.setShopInfo(shopInfo);
                fire.setUserId(userId);
                repoFirebase.save(fire);
            }
        }

    }
}
