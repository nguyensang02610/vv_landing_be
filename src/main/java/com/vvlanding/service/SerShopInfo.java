package com.vvlanding.service;

import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoShopInfo;
import com.vvlanding.dto.UserResponse;
import com.vvlanding.mapper.MapperShopInfo;
import com.vvlanding.payload.SignUpRequest;
import com.vvlanding.repo.RepoShopUserRole;
import com.vvlanding.repo.RepoUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.table.*;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.*;

@Service
public class SerShopInfo {
    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoUser repoUser;

    @Autowired
    RepoShopUserRole repoShopUserRole;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    MapperShopInfo mapperShopInfo;

    public Optional<ShopInfo> findByShopToken(String shopToken) {
        return repoShopInfo.findByShopToken(shopToken);
    }

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public Optional<ShopInfo> find(long id) {
        return repoShopInfo.findById(id);
    }

    public Boolean checkShop(Long id, UserPrincipal currentUser) {
        Optional<ShopUserRole> opShopInfo = repoShopUserRole.findByShopInfoIdAndUserUsername(id, currentUser.getUsername());
        if (!opShopInfo.isPresent()) {
            return false;
        }
        return true;
    }

    public ShopInfo checkShopFindId(Long id, UserPrincipal currentUser) {
        Optional<ShopUserRole> opShopInfo = repoShopUserRole.findByShopInfoIdAndUserUsername(id, currentUser.getUsername());
        return opShopInfo.map(ShopUserRole::getShopInfo).orElse(null);
    }

//    public List<DtoShopInfo> getDtoShopInfoPage(Pageable pageable) {
//        List<ShopInfo> shopInfos = repoShopInfo.findAllBy(pageable);
//        List<DtoShopInfo> dtoShopInfos = new ArrayList<>();
//        for (ShopInfo shopInfo : shopInfos) {
//            DtoShopInfo dtoShopInfo = mapperShopInfo.toDto(shopInfo);
//            dtoShopInfo.setUserId(shopInfo.getUser().getId());
//            dtoShopInfo.setUserName(shopInfo.getUser().getTitle());
//            dtoShopInfos.add(dtoShopInfo);
//        }
//        return dtoShopInfos;
//    }

    public DtoShopInfo InsSent(UserPrincipal currentUser, DtoShopInfo dtoShopInfo) {
        try {
            ShopInfo shopInfo = mapperShopInfo.toEntity(dtoShopInfo);
            Optional<User> opUser = repoUser.findById(currentUser.getId());
            if (opUser.isPresent()){
                for (ShopUserRole s : opUser.get().getShopUserRoles()) {
                    if (s.getRole().equals("ADMIN") || s.getRole().equals("EMPLOYEE")) return null;
                }
                shopInfo.setShopToken(generateNewToken());
                ShopInfo newshopInfo = repoShopInfo.save(shopInfo);
                List<ShopUserRole> shopUserRoleList = new ArrayList<>();
                ShopUserRole shopUserRole = new ShopUserRole();
                shopUserRole.setRole("ADMIN");
                shopUserRole.setUser(opUser.get());
                shopUserRole.setShopInfo(newshopInfo);
                shopUserRoleList.add(shopUserRole);
                repoShopUserRole.saveAll(shopUserRoleList);
                dtoShopInfo.setId(newshopInfo.getId());
                return dtoShopInfo;
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
    public DtoShopInfo update(UserPrincipal currentUser, DtoShopInfo dtoShopInfo) {
        try {
            ShopInfo shopInfo = mapperShopInfo.toEntity(dtoShopInfo);
            shopInfo.setShopToken(generateNewToken());
            ShopInfo newshopInfo = repoShopInfo.save(shopInfo);
            dtoShopInfo.setId(newshopInfo.getId());
            return dtoShopInfo;
        } catch (Exception ex) {
            return null;
        }
    }
    public Object addUserShop(Long shopId, SignUpRequest signUpRequest){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);
            if (!shopInfo.isPresent()){
                response.put("success", false);
                response.put("message", "Không tạo được tài khoản - số điện thoại đã tồn tại");
                return response;
            }
            User result = new User();
            String phone = signUpRequest.getPhone();
            String email = signUpRequest.getEmail();
            String title = signUpRequest.getTitle();
            String image = signUpRequest.getImage();
            String username = signUpRequest.getUsername();
            User user1 = checkPhoneUser(phone);
            if (user1 == null) {
                //Creating user's account if this table is null
                User user = new User(signUpRequest.getImage(), signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getActive(), signUpRequest.getPhone(), signUpRequest.getEmail(), signUpRequest.getTitle());
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setActive(true);
                user.setPhone(phone);
                user.setEmail(email);
                user.setTitle(title);
                user.setImage(image);
                user.setUsername(username);
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
                result = repoUser.save(user);
                ShopUserRole shopUserRole = new ShopUserRole();
                shopUserRole.setRole("EMPLOYEE");
                shopUserRole.setUser(result);
                shopUserRole.setShopInfo(shopInfo.get());
                repoShopUserRole.save(shopUserRole);
                response.put("success", true);
                response.put("message", "Thêm mới tài khoản thành công!");
                response.put("data",result);
            } else {
                response.put("success", false);
                response.put("message", "Không tạo được tài khoản - số điện thoại đã tồn tại");
            }
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", "Tài khoản này đã tồn tại");
        }
        return response;
    }

    public ResponseEntity<?> updateUserShop(SignUpRequest user){
        try {
            String phone = user.getPhone();
            String email = user.getEmail();
            String title = user.getTitle();
//            String image = user.getImage();
            User user1 = checkPhoneUser(phone);
            if (user1 == null) {
                Optional<User> Optional = repoUser.findById(user.getId());
                User userOld = Optional.get();
//                if (user.getPassword() != null){
//                    userOld.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//                }
                userOld.setTitle(title);
                userOld.setPhone(phone);
                userOld.setEmail(email);
//                userOld.setImage(image);
                user1 = repoUser.save(userOld);
            } else {
                Optional<User> Optional = repoUser.findById(user.getId());
                User userOld = Optional.get();
//                if (user.getPassword() != null){
//                    userOld.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//                }
                userOld.setTitle(title);
                userOld.setEmail(email);
//                userOld.setImage(image);
                user1 = repoUser.save(userOld);
            }
            return ResponseEntity.ok(Constant.res("ok",true,null));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }
    public ResponseEntity<?> deleteUserShop(Long id){
        try {
            Optional<User> user = repoUser.findById(id);
            if (!user.isPresent()) return ResponseEntity.ok(Constant.res("không tìm thấy user",false,null));
            Set<ShopUserRole> shopUserRoles = user.get().getShopUserRoles();
            repoShopUserRole.deleteAll(shopUserRoles);
            repoUser.delete(user.get());
            return ResponseEntity.ok(Constant.res("ok",true,null));
        }catch (Exception e){
            return ResponseEntity.ok(Constant.res(e.getMessage(),false,null));
        }
    }

    public ResponseEntity<?> addRoleUser(Long shopUserId,Long userId){
        try {
            List<ShopUserRole> shopUserRoles = repoShopUserRole.findByUserId(shopUserId);
            Optional<User> user = repoUser.findById(userId);
            if (shopUserRoles.size() > 0){
                if (user.isPresent()){
                    ShopInfo shopInfo = shopUserRoles.get(0).getShopInfo();
                    for (ShopUserRole s:
                         user.get().getShopUserRoles()) {
                        if (s.getRole().equals("ADMIN")) return new ResponseEntity<>(Constant.res("Tài khoản chỉ có thể tạo một shop",false,null), HttpStatus.BAD_REQUEST);
                        if (s.getRole().equals("EMPLOYEE")) return new ResponseEntity<>(Constant.res("Tài khoản đã có quyền nhân viên",false,null),HttpStatus.BAD_REQUEST);
                    }
                    ShopUserRole shopUserRole = new ShopUserRole();
                    shopUserRole.setRole("EMPLOYEE");
                    shopUserRole.setUser(user.get());
                    shopUserRole.setShopInfo(shopInfo);
                    repoShopUserRole.save(shopUserRole);

                    return ResponseEntity.ok(Constant.res("OK",true,null));
                }
                return new ResponseEntity<>(Constant.res("Không tìm thấy user",false,null),HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(Constant.res("Không tìm thấy shop",false,null),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(Constant.res(e.getMessage(),false,null),HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> getAllUser(Long shopId){
        try {
            List<ShopUserRole> shopUserRoles = repoShopUserRole.findByShopInfoId(shopId);
            if (shopUserRoles.size() > 0){
                List<UserResponse> users = new ArrayList<>();
                for (ShopUserRole s:
                     shopUserRoles) {
                    users.add(UserResponse.create(s.getUser(),s.getRole()));
                }
                return ResponseEntity.ok(Constant.res("ok",true,users));
            }
            return new ResponseEntity<>(Constant.res("Không tìm thấy shop",false,null),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(Constant.res(e.getMessage(),false,null),HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> getUserByPhone(Long shopId,String phone){
        try {
            Optional<ShopUserRole> shopUserRoles = repoShopUserRole.findByShopInfoIdAndUserPhone(shopId,phone);
            return shopUserRoles.map(shopUserRole -> ResponseEntity.ok(Constant.res("ok", true, UserResponse.create(shopUserRole.getUser(),shopUserRole.getRole())))).orElseGet(() -> new ResponseEntity<>(Constant.res("Không tìm thấy tài khoản", false, null), HttpStatus.OK));
        }catch (Exception e){
            return new ResponseEntity<>(Constant.res(e.getMessage(),false,null),HttpStatus.BAD_REQUEST);
        }
    }

    public ShopInfo FindById(Long id, UserPrincipal currentUser) {
        Optional<ShopUserRole> shopUserRole = repoShopUserRole.findByShopInfoIdAndUserId(id,currentUser.getId());
        return shopUserRole.map(ShopUserRole::getShopInfo).orElse(null);
    }
    public List<ShopInfo> findByUserId(Long id){
        List<ShopUserRole> shopUserRoles = repoShopUserRole.findByUserId(id);
        List<ShopInfo> shopInfos = new ArrayList<>();
        for (ShopUserRole s:shopUserRoles) {
            shopInfos.add(s.getShopInfo());
        }
        return shopInfos;
    }

    public Optional<ShopInfo> FindById2(Long id) {
        return repoShopInfo.findById(id);
    }

    public List<ShopInfo> FindBy(String query) {
        return repoShopInfo.findByTitleContainingOrPhoneContaining(query, query);
    }

    public void Delete(Long id) {
        repoShopInfo.deleteById(id);
    }
    private User checkPhoneUser(String phone) {
        List<User> userOpt = repoUser.findByPhone(phone);
        if (userOpt.size() > 0) {
            return userOpt.get(0);
        }
        return null;
    }


    public int DeleteAll() {
        try {
            repoShopInfo.deleteAll();
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

}