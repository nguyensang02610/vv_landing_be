package com.vvlanding.control;

import com.vvlanding.mapper.MapperBillDetail;
import com.vvlanding.repo.RepoBillDetail;
import com.vvlanding.service.SerBillDetail;
import com.vvlanding.table.BillDetail;
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
@RequestMapping("api/billdt")
public class ContBillDetail {

    @Autowired
    SerBillDetail serBillDetail;

    @Autowired
    MapperBillDetail mapperBillDetail;

    @Autowired
    RepoBillDetail repoBillDetail;

    @RequestMapping(value = "/byid", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> findById(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<BillDetail> optional = serBillDetail.FindById(id);
            BillDetail billDetail = optional.get();
            response.put("data", billDetail);
            response.put("success", true);
            response.put("message", "Ok");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
