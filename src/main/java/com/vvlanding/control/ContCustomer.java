package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoCustomer;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.table.Customer;
import com.vvlanding.service.SerCustomer;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.utils.ListData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/customer")
public class ContCustomer {

    @Autowired
    SerCustomer serCustomer;

    @Autowired
    SerShopInfo serShopInfo;

    // ok
    @RequestMapping(value = "/shop/{shopID}/get", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getCustomerOfShop(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID, Pageable pageable, String query) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopID, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            try {
                boolean q = query == null || query.equals(null);
                List<DtoCustomer> listDtoCustomer = null;
                int start;
                int end;
                int total = 0;
                Pageable pageable1;
                Resp resp = new Resp();
                ListData listData = new ListData();
                if (pageable.getPageNumber() == 0) {
                    if (q) {
                        listDtoCustomer = serCustomer.getCustomerOfShop(shopInfo);
                        total = listDtoCustomer.size();
                    } else {
                        start = (int) PageRequest.of(0, 10).getOffset();
                        listDtoCustomer = serCustomer.findCustomerOfShop(shopInfo, query, query);
                        total = listDtoCustomer.size();
                        end = (start + 10) > listDtoCustomer.size() ? listDtoCustomer.size() : 10;
                        listDtoCustomer = listDtoCustomer.subList(start, end);
                    }
                    resp.setMetaRepo(1, 10, total);
                    repose.put("data", listDtoCustomer);
                } else {
                    pageable1 = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
                    if (q) {
                        listDtoCustomer = serCustomer.getCustomerOfShopAndPage(shopInfo, pageable1);
                        total = serCustomer.getCustomerOfShop(shopInfo).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoCustomer);
                    } else {
                        resp = listData.ListData(serCustomer.findCustomerOfShop(shopInfo, query, query), pageable1);
                        repose.put("data", resp.getList());
                    }
                }
                repose.put("meta", resp.getMeta());
                repose.put("success", true);
                repose.put("message", "Ok");
                return new ResponseEntity<>(repose, HttpStatus.OK);

            } catch (Exception e) {
                repose.put("message", e.getMessage());
                repose.put("success", "false");
                return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
            }
        }
    }

    //-- Xóa
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> delete(@RequestParam Long id) {
        Customer data = serCustomer.FindById(id).orElse(null);
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            serCustomer.Delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //ok
    // update customer
    @RequestMapping(value = "/shop/{shopID}/upd", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> update(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID, @RequestBody DtoCustomer prInput) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
            if (!checkShop) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return serCustomer.updateCustomer(prInput, shopID);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/shop/{shopId}/id/{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getCustomerOfShop(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @PathVariable long id) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            return serCustomer.getDetail(shopInfo, id);
        }
    }

}
