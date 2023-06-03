package com.vvlanding.control;

import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerFirebase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/firebase")
public class ContFirebase {

    @Autowired
    private SerFirebase serFirebase;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    private ResponseEntity<?> save(@CurrentUser UserPrincipal userPrincipal,@RequestBody String body){
        Map<String,Object> map = new HashMap<>();
        try {
            JSONObject object = new JSONObject(body);
            String token = object.getString("token");
            serFirebase.save(token, userPrincipal.getId());
            map.put("success",true);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception e){
            map.put("success",false);
            map.put("message",e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
