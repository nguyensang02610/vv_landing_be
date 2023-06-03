package com.vvlanding.control;

import com.vvlanding.dto.payment.DtoOnePay;
import com.vvlanding.dto.payment.DtoPaymentOnePay;
import com.vvlanding.service.SerOnePay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/onepay")
public class ContOnePay {

    @Autowired
    SerOnePay serOnePay;

    @GetMapping(value = "/create/{shopId}")
    public ResponseEntity<?> create(@RequestBody DtoOnePay dtoOnePay,@PathVariable Long shopId){
        return ResponseEntity.ok(serOnePay.createOnePay(dtoOnePay,shopId));
    }
    @GetMapping(value = "/return")
    public RedirectView returnOnePay(@RequestParam Map<String, String> reqParam){
        RedirectView redirectView = new RedirectView();
        String code = serOnePay.returnOnePay(reqParam);
        if (code.equals("0")){
            redirectView.setUrl("https://landing.vipage.vn/customer?code=0");
        }else redirectView.setUrl("https://landing.vipage.vn/customer?code="+code);
        return redirectView;
    }
    @GetMapping(value = "/ipn")
    public ResponseEntity<?> ipnOnePay(@RequestParam Map<String,String> param){
        return ResponseEntity.ok(serOnePay.ipnOnePay(param));
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<?> insert(@RequestBody DtoPaymentOnePay onePay){
        return serOnePay.configOnePay(onePay);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody DtoPaymentOnePay onePay){
        return serOnePay.updateConfigOnePay(onePay);
    }

}
