package com.vvlanding.control;


import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoLandingPage;
import com.vvlanding.mapper.MapperProduct;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.service.SerTypeLd;
import com.vvlanding.table.LandingPage;
import com.vvlanding.service.SerLandingPage;
import com.vvlanding.table.TypeLd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/ldp")
public class ContLandingPage {

    @Autowired
    SerLandingPage serLandingPage;

    @Autowired
    MapperProduct mapperProduct;

    @Autowired
    SerTypeLd serTypeLd;

    //ok
    //The Cong
    @RequestMapping(value = "", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> getPage(Pageable pageable, String query)
    {
        Resp resp = new Resp();
        Map<String, Object> response = new HashMap<>();
        int total = serLandingPage.GetAll().size();
        try{
            if(pageable.getPageNumber() == 0){
                Pageable pagingSort = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("id")));
                if(query == null){
                    Page<LandingPage> data = serLandingPage.getPage(pagingSort);
                    resp.setData(data.getContent());
                    resp.setTotal(total);
                }
                else{
                    List<LandingPage> data = serLandingPage.FindLandingPageQueryList(query);
                    if(data.size() > 15){
                        List<LandingPage> serviceLandingPageList = data.stream()
                                .sorted(Comparator.comparing(LandingPage::getId))
                                .collect(Collectors.toList());
                        resp.setTotal(serviceLandingPageList.size());
                        resp.setData(serviceLandingPageList.subList(serviceLandingPageList.size() - 12, serviceLandingPageList.size()));
                    }
                    else {
                        resp.setData(data);
                        resp.setTotal(data.size());
                    }
                }
                resp.setPage(pagingSort.getPageNumber() + 1);
                resp.setSize(pagingSort.getPageSize());
            }
            else{
                Pageable pagingSort = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
                if(query == null){
                    Page<LandingPage> data = serLandingPage.getPage(pagingSort);
                    resp.setData(data.getContent());
                    resp.setTotal(total);
                }
                else{
                    List<LandingPage> data = serLandingPage.FindLandingPageByQueryPage(pagingSort, query);
                    resp.setData(data);
                    resp.setTotal(data.size());
                }
                resp.setPage(pageable.getPageNumber());
                resp.setSize(pageable.getPageSize());
            }
            resp.setSuccess(true);
            resp.setMsg("Ok");

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("page", resp.getPage());
            metadata.put("size", resp.getSize());
            metadata.put("total", resp.getTotal());

            response.put("data", resp.getData());
            response.put("success", resp.getSuccess());
            response.put("message", resp.getMsg());
            response.put("metaData", metadata);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            resp.setSuccess(false);
            response.put("success",resp.getSuccess());
            response.put("error", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //- Thêm
    @RequestMapping(value = "/ins", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> InsLanding(@RequestBody DtoLandingPage prInput)
    {
        return new ResponseEntity<>(serLandingPage.InsSent(prInput), HttpStatus.OK);
    }

    @RequestMapping(value = "/byid", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getByID(@RequestParam(value = "id", required = false) Long id) {
        return new ResponseEntity<>(serLandingPage.FindById(id), HttpStatus.OK);
    }

    //-- Xóa
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> delete(@RequestParam Long id) {
        LandingPage data = serLandingPage.FindById(id).orElse(null);
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            serLandingPage.Delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/{typeId}")
    public ResponseEntity<?> LandingList(@PathVariable Long typeId) {

        Optional<TypeLd> typeLd = serTypeLd.getTypeLd(typeId);
        if (!typeLd.isPresent()) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy TypeLd id ",
                    null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        }
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", typeLd.get().getListLandingPage());
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);

    }

    //ok
    @RequestMapping(value = "/typeld", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getById(@RequestParam(value = "id", required = false) Long id) {
        return new ResponseEntity<>(serLandingPage.getAllLdpByTypeId(id), HttpStatus.OK);
    }

}
