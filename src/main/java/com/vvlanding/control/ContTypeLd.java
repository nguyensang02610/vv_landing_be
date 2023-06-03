package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoTypeLd;
import com.vvlanding.repo.RepoTypeLd;
import com.vvlanding.table.TypeLd;
import com.vvlanding.service.SerTypeLd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/typeld")
public class ContTypeLd {

    @Autowired
    SerTypeLd serTypeLd;

    @Autowired
    RepoTypeLd repoTypeLd;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> getAll()
    {
        Resp resp = new Resp();
        Map<String, Object> response =  new HashMap<>();
        try {
            List<TypeLd> data = serTypeLd.GetAll();
            resp.setSuccess(true);
            resp.setMsg("Ok");
            resp.setData(data);

            response.put("data",resp.getData());
            response.put("success",resp.getSuccess());
            response.put("message", resp.getMsg());
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e){
            resp.setSuccess(false);
            response.put("success",resp.getSuccess());
            response.put("error", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> getAllTypeLd(Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DtoTypeLd> data = serTypeLd.getDtoTypeLdPage(pageable);
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

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> findById(@RequestParam long id) {
        return new ResponseEntity<>(serTypeLd.FindById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/title", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> findByTitle(@RequestParam String title,Pageable pageable) {
        return new ResponseEntity<>(serTypeLd.FindByTitle(title,pageable).getContent(), HttpStatus.OK);
    }

    @RequestMapping(value = "/ins", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> insSent(@RequestBody TypeLd prInput) {
        return new ResponseEntity<>(serTypeLd.InsSent(prInput), HttpStatus.OK);
    }

    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> deleteSent(@RequestParam Long id)
    {
        Resp resp = new Resp();
        Map<String, Object> response =  new HashMap<>();
        Optional<TypeLd> optional= repoTypeLd.findById(id);
        TypeLd data  = optional.get();
        if (data.getId() == null)
        {
            resp.setSuccess(false);
            response.put("success",resp.getSuccess());
            response.put("error", "not filter typeLd");
            response.put("message", "Không tìm thấy loại LandingPage trên, vui lòng thử lại !!");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            serTypeLd.Delete(data.getId());
            resp.setSuccess(true);
            resp.setMsg("Đã xóa thành công !!");
            response.put("success", resp.getSuccess());
            response.put("message", resp.getMsg());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }



}
