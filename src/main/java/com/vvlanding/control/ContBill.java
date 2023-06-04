package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoBill;
import com.vvlanding.dto.DtoBillUpdate;
import com.vvlanding.dto.DtoOrder;
import com.vvlanding.mapper.MapperBill;
import com.vvlanding.payload.ResponseStatus;
import com.vvlanding.repo.RepoStatus;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.OrderService;
import com.vvlanding.service.SerBill;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.table.Bill;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/bill")
public class ContBill {

    private static final Logger logger = LoggerFactory.getLogger(ContBill.class);

    @Autowired
    SerBill serBill;

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    RepoStatus repoStatus;

    @Autowired
    MapperBill mapperBill;

    @Autowired
    OrderService orderService;

    // ok
    // get bill of shop and query(customer title, customer phone or code_bill or satus_ship_name)
    @RequestMapping(value = "/shop/{shopId}/get", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getBillOfShop(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, String query, @RequestParam String time, Pageable pageable) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            try {
                int start;
                int end;
                List<DtoBill> listDtoBill;
                Resp resp = new Resp();
                Pageable pageable1;
                int total = 0;
                boolean b = query == null || query.equals(null);
                if (pageable.getPageNumber() == 0 || pageable.getPageNumber() == 1) {
                    if (b) {
                        start = (int) PageRequest.of(0, 10).getOffset();
                        listDtoBill = serBill.getBillOfShop(shopInfo, time);
                        total = listDtoBill.size();
                        end = (start + 10) > listDtoBill.size() ? listDtoBill.size() : 10;
                        resp.setMetaRepo(1, 10, listDtoBill.size());
                        listDtoBill = listDtoBill.subList(start, end);
                    } else {
                        listDtoBill = serBill.getBillOfShopByQuery(shopInfo, query, query, time);
                        total = listDtoBill.size();
                        start = (int) PageRequest.of(0, 10).getOffset();
                        end = (start + 10) > listDtoBill.size() ? listDtoBill.size() : 10;
                        resp.setMetaRepo(1, 10, listDtoBill.size());
                        listDtoBill = listDtoBill.subList(start, end);
                    }
                    resp.setMetaRepo(1, 10, total);
                    repose.put("data", listDtoBill);
                } else {
                    pageable1 = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
                    if (b) {
                        listDtoBill = serBill.getBillOfShopPage(shopInfo,time, pageable1);
                        total = serBill.getBillOfShop(shopInfo,time).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoBill);
                    } else {
                        listDtoBill = serBill.getBillOfShopByQueryPage(shopInfo, query, query,time, pageable1);
                        total = serBill.getBillOfShopByQuery(shopInfo, query, query, time).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoBill);
                    }
                }
                repose.put("meta", resp.getMeta());
                repose.put("success", true);
                repose.put("message", "Oke");
                return new ResponseEntity<>(repose, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                repose.put("success", false);
                repose.put("error", e.getMessage());
                return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
            }
        }
    }

    // ok
    @RequestMapping(value = "/shop/{shopId}/get/{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getBillOfShop(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @PathVariable long id) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            return serBill.getDetailBill(shopInfo, id);
        }
    }

    @RequestMapping(value = "/shop/{shopId}/get2/{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getBillOfShop2(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @PathVariable long id) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            return serBill.getDetailBill2(shopInfo, id);
        }
    }


    @RequestMapping(value = "/shop/{shopId}/updatestatus", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateStatus(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @RequestBody ResponseStatus responseStatus) {
        Map<String, Object> response = new HashMap<>();
        try {
            ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
            if (shopInfo == null || shopInfo.equals(null)) {
                response.put("message", "Không tìm thấy thông tin shop");
                response.put("success", "false");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                return serBill.updateStatus(shopInfo, responseStatus);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ok
    //Thống kê hóa đơn theo năm (key: year value: giá trị của year)
    @RequestMapping(value = "/shop/{shopId}/report", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> findByAbout(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @RequestParam String time,String channel , String shipPartner) {
        Map<String, Object> response = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            response.put("message", "Không tìm thấy thông tin shop");
            response.put("success", "false");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return serBill.ReportServiceShopOfAbout(shopId, time,channel,shipPartner);

    }

    // ok
    //Tổng của từng trạng thái đơn hàng
    @RequestMapping(value = "/shop/{shopId}/totalStatus", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> findByTotal(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @RequestParam String time) {
        Map<String, Object> response = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            response.put("message", "Không tìm thấy thông tin shop");
            response.put("success", "false");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return serBill.TotalStatus(shopId, time );
    }


    // Tìm kiếm từ ngày đến ngày (key:query value:05-01-2020/20-03-2020)
    @RequestMapping(value = "/findinrange", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> getAllNewShop(String channel , String shipPartner,String query, Pageable pageable) {
        Resp resp = new Resp();
        Map<String, Object> response = new HashMap<>();
        Page<Bill> data = serBill.getAllBillInMonth(channel,shipPartner,query, pageable);
        if (data.getContent().isEmpty()) {
            resp.setSuccess(false);
            resp.setMsg("Không có dữ liệu hoặc Dữ liệu bạn vừa nhập không hợp lệ. ");
            response.put("success", resp.getSuccess());
            response.put("message", resp.getMsg());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            resp.setSuccess(true);
            resp.setMsg("Ok");
            resp.setData(data.getContent());
            resp.setPage(data.getPageable().getPageNumber());
            resp.setSize(data.getPageable().getPageSize());
            resp.setTotal(data.getTotalElements());

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
    }

    // ok - Chuyển trạng thái Đơn Mới -> Đã xác nhận
    @RequestMapping(value = "shop/{shopId}/update/list/status", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateListStatus(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @RequestParam List<Long> id) {
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop",
                    null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        } else {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serBill.updateListStatus(shopId, id));
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
        }
    }


    // ok - Chuyển trạng thái Đã xem đơn
    @RequestMapping(value = "shop/{shopId}/view/true", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateViewBill(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId, @RequestParam Long id) {
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop",
                    null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        } else {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serBill.updateViewBill(shopId, id));
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/bill/{shopId}" ,method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity deleteBill(@CurrentUser UserPrincipal currentUser,@PathVariable long shopId , @RequestParam long billId){
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop",
                    null);
            return new ResponseEntity(statusMessage, HttpStatus.BAD_REQUEST);
        }else {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serBill.deleteBill(billId, shopId));
            return new ResponseEntity(statusMessage, HttpStatus.OK);
        }
    }

    // ok
    //đơn hàng chưa xem
    @RequestMapping(value = "{shopId}/new", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> findByTotalView(@CurrentUser UserPrincipal currentUser, @PathVariable long shopId) {
        Map<String, Object> response = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            response.put("message", "Không tìm thấy thông tin shop");
            response.put("success", "false");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return serBill.TotalViewStatus(shopId);
    }
    @RequestMapping(value = "update/bill/{shopId}",method = RequestMethod.POST)
    public ResponseEntity updateBill(@PathVariable Long shopId ,@CurrentUser UserPrincipal currentUser, @RequestBody DtoBillUpdate dtoBill){
        Map<String, Object> response = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            response.put("message", "Không tìm thấy thông tin shop");
            response.put("success", "false");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return serBill.updateBill(shopId, dtoBill);
    }

    //Cong - ins order landing
    @RequestMapping(value = "/order/{shopId}", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> InsLanding(@RequestBody DtoOrder prInput, @CurrentUser UserPrincipal currentUser,@PathVariable Long shopId) {
        Map<String, Object> response = new HashMap<>();
        try {
            ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
            if (shopInfo == null || shopInfo.equals(null)) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(orderService.InsSent2(prInput), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}
