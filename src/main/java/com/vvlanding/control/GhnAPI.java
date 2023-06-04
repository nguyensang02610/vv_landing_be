package com.vvlanding.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vvlanding.dto.shipped.ghn.FeeGHN;
import com.vvlanding.dto.shipped.viettel.LoginViettel;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerGHN;
import com.vvlanding.service.SerShipper;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/ghn")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GhnAPI {

    @Autowired
    private SerGHN serGHN;

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    SerShipper serShipper;

    @RequestMapping(value = "/order/{shopId}", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> orderGHN(@CurrentUser UserPrincipal currentUser, @RequestParam String billId, @RequestParam String name, @PathVariable long shopId, @RequestParam long shopGhnId) throws URISyntaxException {
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop",
                    null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        } else {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serGHN.createOrder(billId, name, shopId, shopGhnId));
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/cancel/{shopId}", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> cancel(@CurrentUser UserPrincipal currentUser, @RequestParam String id, @PathVariable long shopId, @RequestParam String shopGhnId) throws URISyntaxException, JsonProcessingException {
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop",
                    null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        } else {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serGHN.cancel(id, shopId, shopGhnId));
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/fee/{shopId}", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> fee(@CurrentUser UserPrincipal currentUser, @RequestBody FeeGHN feeGHN, @RequestParam long billId, @RequestParam String name, @PathVariable long shopId, @RequestParam long shopGhnId) throws URISyntaxException, JsonProcessingException {
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop",
                    null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        } else {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serGHN.fee(feeGHN, billId, name, shopId, shopGhnId));
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
        }
    }
    @RequestMapping(value = "/fee/customer/{shopId}", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> feeCustomer(@RequestBody FeeGHN feeGHN, @RequestParam String name, @PathVariable long shopId, @RequestParam long shopGhnId) throws URISyntaxException, JsonProcessingException {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serGHN.feeCustomer(feeGHN, name, shopId, shopGhnId));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

    //Thêm đơn vị vận chuyển
    @RequestMapping(value = "/insert/shipper/{shopId}", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> insertShipper(@RequestBody LoginViettel login, @PathVariable Long shopId, @RequestParam String token, @RequestParam String name ,@RequestParam Long type) throws URISyntaxException, JsonProcessingException {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serShipper.insertShipper(login,shopId,token,name,type));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }
    @RequestMapping(value = "get/shop/ghn/{shopId}",method = RequestMethod.GET)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity getShopGhn(@CurrentUser UserPrincipal currentUser,@PathVariable long shopId){
        Map<String,Object> response = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            response.put("message","không tìm thấy thông tin shop");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }else {
            response.put("data",serGHN.getShopGhn(shopId));
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }


}
