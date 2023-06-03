package com.vvlanding.service;

import com.vvlanding.dto.DtoShopInfo;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.repo.RepoShopUserRole;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.table.ShopUserRole;
import com.vvlanding.table.User;
import com.vvlanding.repo.RepoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SerUser {
    @Autowired
    RepoUser repoUser;

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoShopUserRole repoShopUserRole;

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional findUserByUserName(String userName) {
        return repoUser.findByUsername(userName);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        return repoUser.save(user);
    }

    public void saveWithoutEncode(User user) {
        repoUser.save(user);
    }

    public Optional<User> FindById(Long id) {
        return repoUser.findById(id);
    }

    public Object getShopInfoByUserId(UserPrincipal currentUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ShopUserRole> dataShopInfo = repoShopUserRole.findByUserId(currentUser.getId());
            List<DtoShopInfo> dataShop = new ArrayList<>();
            ShopInfo shopInfo = null;
            for (ShopUserRole s:
                 dataShopInfo) {
                DtoShopInfo shopInfo1 = dtoShopInfo(s.getShopInfo(),s.getUser().getId(),s.getUser().getTitle());
                dataShop.add(shopInfo1);
            }
            response.put("success", true);
            response.put("data", dataShop);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }
    public DtoShopInfo dtoShopInfo(ShopInfo shopInfo,Long userId,String userName){
        DtoShopInfo dataShop = new DtoShopInfo();
        dataShop.setId(shopInfo.getId());
        dataShop.setTitle(shopInfo.getTitle());
        dataShop.setLogo(shopInfo.getLogo());
        dataShop.setAddress(shopInfo.getAddress());
        dataShop.setPhone(shopInfo.getPhone());
        dataShop.setEmail(shopInfo.getEmail());
        dataShop.setManager(shopInfo.getManager());
        dataShop.setTelegram(shopInfo.getTelegram());
        dataShop.setYoutube(shopInfo.getYoutube());
        dataShop.setWebsite(shopInfo.getWebsite());
        dataShop.setDistrict(shopInfo.getDistrict());
        dataShop.setProvince(shopInfo.getProvince());
        dataShop.setWard(shopInfo.getWard());
        dataShop.setUserId(userId);
        dataShop.setUserName(userName);
        dataShop.setShopToken(shopInfo.getShopToken());
        return dataShop;
    }

    public Optional<User> getUser(UserPrincipal currentUser) {
        return repoUser.findById(currentUser.getId());
    }

    public void Delete(Long id) {
        repoUser.deleteById(id);
    }

    private User checkPhoneUser(String phone) {
        List<User> userOpt = repoUser.findByPhone(phone);
        if (userOpt.size() > 0) {
            return userOpt.get(0);
        }
        return null;
    }

    public User AccountUpdate(User user, UserPrincipal currentUser) {
        String phone = user.getPhone();
        String email = user.getEmail();
        String title = user.getTitle();
        String image = user.getImage();
        User user1 = checkPhoneUser(phone);
        if (user1 == null) {
            Optional<User> Optional = repoUser.findByUsername(currentUser.getUsername());
            User userOld = Optional.get();
            userOld.setTitle(title);
            userOld.setPhone(phone);
            userOld.setEmail(email);
            userOld.setImage(image);
            user1 = repoUser.save(userOld);
        } else {
            Optional<User> Optional = repoUser.findByUsername(currentUser.getUsername());
            User userOld = Optional.get();
            userOld.setTitle(title);
            userOld.setEmail(email);
            userOld.setImage(image);
            user1 = repoUser.save(userOld);
        }
        return user1;
    }

    public User PasswordChange(User user, UserPrincipal currentUser) {
        Optional<User> Optional = repoUser.findByUsername(currentUser.getUsername());
        User userOld = Optional.get();
        userOld.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return repoUser.save(userOld);
    }

}
