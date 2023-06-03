package com.vvlanding.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vvlanding.dto.shipped.viettel.FeeViettelDTO;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerAddressViettel;
import com.vvlanding.service.SerViettel;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/ghvt")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ViettelPostAPI {

    @Autowired
    SerViettel serViettel;

    @Autowired
    SerAddressViettel serAddressViettel;

    @Autowired
    RepoShopInfo repoShopInfo;

    @RequestMapping(value = "/order/{shopId}",method = RequestMethod.POST)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> order(@CurrentUser UserPrincipal userPrincipal, @RequestParam long billId, @RequestParam String name, @PathVariable long shopId) throws URISyntaxException, JsonProcessingException {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serViettel.orderViettel(billId,name,shopId,userPrincipal.getTitle()));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/fee/{shopId}",method = RequestMethod.POST)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> fee(@RequestBody FeeViettelDTO feeViettelDTO,@RequestParam long billId,@RequestParam String name, @PathVariable long shopId) throws URISyntaxException, JsonProcessingException {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serViettel.fee(feeViettelDTO,billId,name,shopId));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{shopId}",method = RequestMethod.POST)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> update(@RequestParam long id, @PathVariable long shopId) throws URISyntaxException, JsonProcessingException {
        Map<String, Object> response = new HashMap<>();

        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);

        if (!shopInfo.isPresent()){
            response.put("success","false");
            response.put("message","Không tìm thấy thông tin shop");
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serViettel.update(id));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }



}
