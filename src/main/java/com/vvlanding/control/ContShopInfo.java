package com.vvlanding.control;

import com.vvlanding.config.role.ADMIN;
import com.vvlanding.config.role.EMPLOYEE;
import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoShopInfo;
import com.vvlanding.payload.SignUpRequest;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerUser;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/shopinfo")
public class ContShopInfo {
    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    SerUser serUser;

    // ok
    // API tạo Shop
    @RequestMapping(value = "/ins", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> InsShop(@CurrentUser UserPrincipal currentUser, @RequestBody DtoShopInfo prInput) {
        if (prInput.getId() == null)
            prInput.setId(0L);
        Map<String, Object> response = new HashMap<>();
        try {
            DtoShopInfo data = serShopInfo.InsSent(currentUser, prInput);
            response.put("data", data);
            response.put("success", true);
            response.put("message", "Ok");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ADMIN
    public ResponseEntity<Map<String, Object>> update(@CurrentUser UserPrincipal currentUser, @RequestBody DtoShopInfo prInput) {
        if (prInput.getId() == null)
            prInput.setId(0L);
        Map<String, Object> response = new HashMap<>();
        try {
            DtoShopInfo data = serShopInfo.update(currentUser, prInput);
            response.put("data", data);
            response.put("success", true);
            response.put("message", "Ok");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //-- Xóa
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ADMIN
    public ResponseEntity<?> delete(@CurrentUser UserPrincipal currentUser, @RequestParam Long id) {
        ShopInfo data = serShopInfo.FindById(id, currentUser);
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            serShopInfo.Delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //-- Xóa hết
    @RequestMapping(value = "/delall", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ADMIN
    public ResponseEntity<?> Del() {
        return new ResponseEntity<>(serShopInfo.DeleteAll(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/{userId}")
    public ResponseEntity<?> ShopInfoList(@CurrentUser UserPrincipal currentUser, @PathVariable Long userId) {
        Optional<User> user = serUser.getUser(currentUser);
        if (!user.isPresent()) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy UserId ",
                    null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        }
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serShopInfo.findByUserId(currentUser.getId()));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

//    @RequestMapping(value = "/add/role",method = RequestMethod.POST)
//    @ADMIN
//    public ResponseEntity<?> addRoleUser(@CurrentUser UserPrincipal userPrincipal,@RequestParam Long userId){
//        return serShopInfo.addRoleUser(userPrincipal.getId(),userId);
//    }

    @RequestMapping(value = "/add/user",method = RequestMethod.POST)
    @ADMIN
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> addUser(@CurrentUser UserPrincipal currentUser, @RequestParam Long shopId, @RequestBody SignUpRequest signUpRequest){
        ShopInfo data = serShopInfo.FindById(shopId, currentUser);
        return new ResponseEntity<>(serShopInfo.addUserShop(data.getId(),signUpRequest), HttpStatus.OK);
    }

    @RequestMapping(value = "/update/user",method = RequestMethod.POST)
    @ADMIN
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateUser(@RequestBody SignUpRequest signUpRequest){
        return new ResponseEntity<>(serShopInfo.updateUserShop(signUpRequest),HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/user",method = RequestMethod.POST)
    @ADMIN
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> deleteUser(@RequestParam Long userId){
        return new ResponseEntity<>(serShopInfo.deleteUserShop(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/get/user",method = RequestMethod.GET)
    @ADMIN
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getAllUser(@RequestParam Long shopId){
        return serShopInfo.getAllUser(shopId);
    }

    @RequestMapping(value = "/get/user/phone",method = RequestMethod.GET)
    @ADMIN
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> deleteUser(@RequestParam Long shopId,@RequestParam String phone){
        return serShopInfo.getUserByPhone(shopId,phone);
    }

}
