package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.service.SerProductCategorie;
import com.vvlanding.table.ProductCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/proCategories")

public class ContProductCategorie {

    @Autowired
    SerProductCategorie serProductCategorie;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getPage(Pageable pageable) {
        return new ResponseEntity<>(serProductCategorie.getPage(pageable).getContent(), HttpStatus.OK);
    }

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> findById(@RequestParam Long id) {
        Resp resp = new Resp();
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ProductCategories> optional = serProductCategorie.FindById(id);
            ProductCategories ProductCategories = optional.get();
            response.put("data", ProductCategories);
            response.put("success", true);
            response.put("message", "Ok");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            resp.setMsg(" Không tìm thấy dữ liệu vừa nhập trong CSDL. ");
            response.put("message", resp.getMsg());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/title", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> findByTitle(@RequestParam String title) {
        Resp resp = new Resp();
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ProductCategories> optional = serProductCategorie.FindByTitle(title);
            ProductCategories ProductCategories = optional.get();
            response.put("data", ProductCategories);
            response.put("success", true);
            response.put("message", "Ok");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            resp.setMsg(" Không tìm thấy dữ liệu vừa nhập trong CSDL. ");
            response.put("message", resp.getMsg());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/ins", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> InsSent(@RequestBody ProductCategories prInput) {
        Resp resp = new Resp();
        if (prInput.getId() == null)
            prInput.setId(0L);
        Map<String, Object> response = new HashMap<>();
        try {
            ProductCategories data = serProductCategorie.InsSent(prInput);

            response.put("data", data);
            response.put("success", true);
            response.put("message", "Ok");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            resp.setMsg("Trùng dữ liệu. ");
            response.put("success", false);
            response.put("message", resp.getMsg());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> deleteSent(@RequestParam Long id) {
        ProductCategories data = serProductCategorie.FindById(id).orElse(null);
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            serProductCategorie.Delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}

