package com.vvlanding.service;

import com.vvlanding.dto.DtoConfig;
import com.vvlanding.mapper.MapperConfig;
import com.vvlanding.repo.RepoConfig;
import com.vvlanding.repo.RepoRefLandingPageUser;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.repo.RepoShopUserRole;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.table.Config;
import com.vvlanding.table.RefLandingPageUser;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.table.ShopUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SerConfig {
    @Autowired
    RepoConfig repoConfig;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    SerConfig serConfig;

    @Autowired
    RepoShopUserRole repoShopUserRole;

    @Autowired
    MapperConfig mapperConfig;

    @Autowired
    RepoRefLandingPageUser repoRefLandingPageUser;

    public List<Config> findByRefLandingPageUser(RefLandingPageUser refLandingPageUser) {
        return repoConfig.findByRefLandingPageUser(refLandingPageUser);
    }

    public List<DtoConfig> getDTOConfigPage(Pageable pageable) {
        List<Config> configList = repoConfig.findAllBy(pageable);
        List<DtoConfig> dtoConfigs = new ArrayList<>();
        for (Config config : configList) {
            DtoConfig dtoConfig = mapperConfig.toDto(config);
            dtoConfig.setRefId(config.getRefLandingPageUser().getId());
            dtoConfigs.add(dtoConfig);
        }
        return dtoConfigs;
    }

    public static DtoConfig GetAllConfig(RefLandingPageUser refLandingPageUser, Config config) {
        DtoConfig dtoConfig = new DtoConfig();
        dtoConfig.setId(config.getId());
        dtoConfig.setTitle(config.getTitle());
        dtoConfig.setDescription(config.getDescription());
        dtoConfig.setKeywords(config.getKeywords());
        dtoConfig.setPhone(config.getPhone());
        dtoConfig.setZalo(config.getZalo());
        dtoConfig.setFacebook(config.getFacebook());
        dtoConfig.setGgAnalytics(config.getGgAnalytics());
        dtoConfig.setFacebookPlug(config.getFacebookPlug());
        dtoConfig.setPlugOther(config.getPlugOther());
        dtoConfig.setShopId(config.getShopId());
        dtoConfig.setRefId(refLandingPageUser.getId());
        return dtoConfig;
    }

    public Object InsSent(DtoConfig dtoConfig, UserPrincipal currentUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Config config = new Config();


            Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(dtoConfig.getRefId());
            RefLandingPageUser refLandingPageUser = opRefLandingPageUser.get();
            if (!opRefLandingPageUser.isPresent()) {
                response.put("message", "Không tìm thấy thông tin Ref !!");
                response.put("success", false);
                return response;
            }
            List<ShopUserRole> opShopInfo = repoShopUserRole.findByUserId(currentUser.getId());
            if (!(opShopInfo.size() > 0)) {
                response.put("message", "Không tìm thấy thông tin Shop !!");
                response.put("success", false);
                return response;
            }
            ShopInfo shopInfo = opShopInfo.get(0).getShopInfo();
            config.setShopId(shopInfo.getId());
            config.setRefLandingPageUser(refLandingPageUser);
            config.setTitle(dtoConfig.getTitle());
            config.setDescription(dtoConfig.getDescription());
            config.setKeywords(dtoConfig.getKeywords());
            config.setPhone(dtoConfig.getPhone());
            config.setZalo(dtoConfig.getZalo());
            config.setFacebook(dtoConfig.getFacebook());
            config.setGgAnalytics(dtoConfig.getGgAnalytics());
            config.setFacebookPlug(dtoConfig.getFacebookPlug());
            config.setPlugOther(dtoConfig.getPlugOther());
            Config config1 = repoConfig.save(config);
            dtoConfig.setId(config1.getId());
            response.put("data", dtoConfig);
            response.put("success", true);
            return response;
        } catch (Exception ex) {
            response.put("data", ex);
            response.put("success", false);
            return response;
        }
    }

    public Boolean checkShop(Long id, UserPrincipal currentUser) {
        Optional<ShopUserRole> opShopInfo = repoShopUserRole.findByShopInfoIdAndUserId(id, currentUser.getId());
        if (!opShopInfo.isPresent()) {
            return false;
        }
        return true;
    }

    public Boolean checkRef(Long id) {
        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(id);
        if (!opRefLandingPageUser.isPresent()) {
            return false;
        }
        return true;
    }

    // cập nhật thông tin sản phẩm
    public ResponseEntity updateConfig(DtoConfig dtoConfig, Long refId) {
        Map<String, Object> response = new HashMap<>();

        // check Config
        Optional<Config> opConfig = repoConfig.findByIdAndRefLandingPageUser_id(dtoConfig.getId(), refId);
        if (!opConfig.isPresent()) {
            response.put("mesager", "không tìm thấy thông tin config  !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Config config = opConfig.get();
        config.setTitle(dtoConfig.getTitle());
        config.setDescription(dtoConfig.getDescription());
        config.setKeywords(dtoConfig.getKeywords());
        config.setPhone(dtoConfig.getPhone());
        config.setZalo(dtoConfig.getZalo());
        config.setFacebook(dtoConfig.getFacebook());
        config.setGgAnalytics(dtoConfig.getGgAnalytics());
        config.setFacebookPlug(dtoConfig.getFacebookPlug());
        config.setPlugOther(dtoConfig.getPlugOther());
        Config updateConfig = repoConfig.save(config);
        response.put("data", updateConfig);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
