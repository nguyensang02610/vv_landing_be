package com.vvlanding.control;

import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerShipper;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/shipper")
public class ContShipper {
    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    SerShipper serShipper;


    @RequestMapping(value = "/shop/{shopId}", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getShipperbyShopId(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @RequestParam String name) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            return serShipper.GetShipper(name, shopInfo);
        }
    }



    // get tất cả đvvc của shop
    @RequestMapping(value = "/get/all/{shopId}", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getAll(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        }
        return serShipper.getAllShipper(shopId);
    }
}
