package com.vvlanding.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vvlanding.dto.shipped.ghtk.ShipPackageDTO;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerGhtkService;
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
@RequestMapping("api/ghtk")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GhtkAPI {

    @Autowired
    SerGhtkService serGhtkService;

    @Autowired
    RepoShopInfo repoShopInfo;

    @RequestMapping(value = "/order/{shopId}",method = RequestMethod.POST)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> orderGHTK(@CurrentUser UserPrincipal userPrincipal, @RequestParam long billId, @RequestParam String name, @PathVariable long shopId) throws URISyntaxException, JsonProcessingException {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serGhtkService.createShip(billId,name,shopId,userPrincipal.getTitle()));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/status",method = RequestMethod.GET)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> ghtkStatus(@RequestParam long id) throws URISyntaxException {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serGhtkService.statusResponse(id));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage,HttpStatus.OK);
    }

    @RequestMapping(value = "/cancel/{shopId}",method = RequestMethod.POST)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity ghtkCancel(@RequestParam long id,@PathVariable long shopId) throws URISyntaxException {
        Map<String, Object> response = new HashMap<>();

        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);

        if (!shopInfo.isPresent()){
            response.put("success","false");
            response.put("message","Không tìm thấy thông tin shop");
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }

        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serGhtkService.cancel(id));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }

    @RequestMapping(value = "/fee/{shopId}",method = RequestMethod.GET)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> ghtkFee(@RequestParam long billId,@RequestParam String name , @PathVariable long shopId){
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serGhtkService.calculateFee(billId,name,shopId));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage,HttpStatus.OK);
    }

    @RequestMapping(value = "/fee/customer/{shopId}",method = RequestMethod.GET)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity<?> customerFee(@RequestBody ShipPackageDTO ship,@RequestParam String name , @PathVariable long shopId){
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serGhtkService.calculateFeeCustomes(ship,name,shopId));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage,HttpStatus.OK);
    }


}
