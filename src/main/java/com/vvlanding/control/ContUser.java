package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.repo.RepoUser;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.table.User;
import com.vvlanding.service.SerUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/user")
public class ContUser {

    @Autowired
    SerUser serUser;

    @Autowired
    RepoUser repoUser;

    // ok
    //-- Lấy toàn bộ danh sách shop theo userId
    @RequestMapping(value = "/getbyid/{userId}/shop", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getByID(@CurrentUser UserPrincipal currentUser, @PathVariable Long userId) {
        return new ResponseEntity<>(serUser.getShopInfoByUserId(currentUser), HttpStatus.OK);
    }

    // ok
    //-- Lấy thông tin user theo userId
    @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getUserById(@CurrentUser UserPrincipal currentUser, @RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> data = serUser.getUser(currentUser);
        User user = data.get();
        if (!data.isPresent()) {
            response.put("success", false);
            response.put("data", null);
        } else {
            response.put("success", true);
            response.put("data", user);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ok
    //-- Sửa thông tin user
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> updateSent(@CurrentUser UserPrincipal currentUser, @RequestBody User User) {
        Resp resp = new Resp();
        Map<String, Object> response = new HashMap<>();
        try {
            User user = serUser.AccountUpdate(User, currentUser);
            resp.setData(user);
            response.put("data", resp.getData());
            response.put("success", true);
            response.put("message", "ok");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ok
    //-- Đổi mật khẩu
    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> ChangePassword(@CurrentUser UserPrincipal currentUser, @RequestBody User user) {
        return new ResponseEntity<>(serUser.PasswordChange(user, currentUser), HttpStatus.OK);
    }

    @RequestMapping(value = "/update/fb",method = RequestMethod.POST)
    public ResponseEntity<?> getByFbID(@CurrentUser UserPrincipal userPrincipal,@RequestParam String fbId){
        try {
            User user = repoUser.getOne(userPrincipal.getId());
            user.setFbId(fbId);
            repoUser.save(user);
            return new ResponseEntity<>("200",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("400",HttpStatus.OK);
        }
    }
}

