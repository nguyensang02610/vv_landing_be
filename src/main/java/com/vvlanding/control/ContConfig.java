package com.vvlanding.control;

import com.vvlanding.dto.DtoConfig;
import com.vvlanding.dto.DtoCustomer;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerConfig;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/config")
public class ContConfig {

    @Autowired
    SerConfig serConfig;

    //-- get all config
    @RequestMapping(value = "/getConfig_landing", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> getAllConfig(Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DtoConfig> data = serConfig.getDTOConfigPage(pageable);
            response.put("data", data);
            response.put("success", true);
            response.put("message", "Ok");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //ok
    //- Thêm
    @RequestMapping(value = "/ins", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> InsRef(@CurrentUser UserPrincipal currentUser, @RequestBody DtoConfig prInput) {
        Object data = serConfig.InsSent(prInput, currentUser);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    //ok
    // update Config
    @RequestMapping(value = "/ref/{refID}/upd", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateRef(@CurrentUser UserPrincipal currentUser,@RequestParam long shopID, @PathVariable long refID, @RequestBody DtoConfig prInput) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serConfig.checkShop(shopID,currentUser);
            if (!checkShop) {
                response.put("message", "không tìm thấy thông tin Shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }else {
                boolean checkRef = serConfig.checkRef(refID);
                if (!checkRef) {
                    response.put("message", "không tìm thấy thông tin Ref !!!!");
                    response.put("success", false);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                return serConfig.updateConfig(prInput, refID);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}