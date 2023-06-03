package com.vvlanding.control;

import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.shopee.order.order.OrderServices;
import com.vvlanding.shopee.order.order.Orders;
import com.vvlanding.shopee.service.shopee.ShopAuthServices;
import com.vvlanding.shopee.service.shopee.ShopeeService;
import com.vvlanding.table.ChannelShopee;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/shopee/order")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ShopeeOrderAPI {

    private long partnerOrderID = Iconstants.partner_id_order_v2;

    private String partnerOrderKey = Iconstants.partner_key_order_v2;

    @Autowired
    ShopeeService shopeeService;

    @Autowired
    OrderServices orderServices;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    SerShopInfo serShopInfo;

    private String domain = Iconstants.domain;

    @RequestMapping(value = "/get/shop/{shopID}", method = RequestMethod.GET)
    public ResponseEntity getShopAppOrder(@PathVariable long shopID, @RequestParam String code, @RequestParam(name = "shop_id") long shopeeShopId) {
        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopID);
        Map<String, Object> response = new HashMap<>();
        if (!shopInfo.isPresent()) {
            response.put("message", "không tìm thấy thông tin shop");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        try {
            shopeeService.getAccess_token(shopInfo.get(), code, shopeeShopId, Iconstants.partner_key_order_v2, Iconstants.partner_id_order_v2, 2L);

            List<Orders> orders = orderServices.fetchOrdersV2(shopInfo.get(), shopeeShopId);

            orderServices.fetchListOrderDetailV2(shopInfo.get(), shopeeShopId, orders);

            response.put("success", true);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/cancel/auth/order")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity cancelAppOrderV2() {
        String shopeeRedirectUrl = domain + "/shopee";
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", ShopAuthServices.cancelAppV2(shopeeRedirectUrl, partnerOrderKey, partnerOrderID));
        return new ResponseEntity(statusMessage, HttpStatus.OK);
    }


    // get url kết nối app order
    @GetMapping(value = "/get/url/v2")
    public ResponseEntity getAuthAppOrder() {
        String shopeeRedirectUrl = domain + "/bill";
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", ShopAuthServices.authShopV2(partnerOrderID, shopeeRedirectUrl, partnerOrderKey));
        return new ResponseEntity(statusMessage, HttpStatus.OK);
    }


    @RequestMapping(value = "/get/access/token/{shopId}", method = RequestMethod.GET)
    public ResponseEntity getTokenAppOrder(@PathVariable long shopId, @RequestParam long shop_id, @RequestParam String code) {
        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", shopeeService.getAccess_token(shopInfo.get(), code, shop_id, Iconstants.partner_key_order_v2, Iconstants.partner_id_order_v2, 2L));
        return new ResponseEntity(statusMessage, HttpStatus.OK);
    }


    @RequestMapping(value = "/get/{shopId}/list/order/v2", method = RequestMethod.GET)
    public ResponseEntity<?> getListOrderV2(@PathVariable long shopId, @RequestParam(name = "shop_id") List<Long> shopeeShopId) {
        Map<String, Object> response = new HashMap<>();
        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);
        if (!shopInfo.isPresent()) {
            response.put("message", "không tìm thấy thông tin shop");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            for (Long l : shopeeShopId) {
                String access_token = shopeeService.getToken(l, Iconstants.partner_key_order_v2, Iconstants.partner_id_order_v2, 2L);
                if (access_token.equals("invalid")){
                    ChannelShopee channelShopee = shopeeService.getShopeeById(l);
                    if (channelShopee != null) response.put("message", "token shopee " + channelShopee.getShopeeShopName() + " đã hết hạn");
                    response.put("success", false);
                    response.put("data",null);
                    return ResponseEntity.badRequest().body(response);
                }
                List<Orders> fetchOrders = orderServices.fetchOrdersV2(shopInfo.get(), l);
                try {
                    orderServices.fetchListOrderDetailV2(shopInfo.get(), l, fetchOrders);
                    response.put("success", true);
                    response.put("data",null);
                    response.put("message","");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception e) {
                    response.put("success", false);
                    response.put("message", e.getMessage());
                    response.put("data",null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data",null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("success", false);
        response.put("data",null);
        response.put("message","");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "/order/detail",method = RequestMethod.GET)
    public ResponseEntity getDetail(@RequestParam List<String> order,@RequestParam Long id){
        Optional<ShopInfo> shopInfo = repoShopInfo.findById(id);
        return new ResponseEntity(orderServices.getDetailV2(shopInfo.get(),493310717L,order),HttpStatus.OK);
    }

    //*
    // Test......................
    // */
    @GetMapping(value = "/get/url/v2/test")
    public ResponseEntity getAuthAppOrderTest() {
        String shopeeRedirectUrl = "http://localhost:6868/bill";
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",ShopAuthServices.authShopV2Test(1001118,shopeeRedirectUrl,"7c31e0d6f76dbadd99aa848371d6e5a3b97b00dae2d30b4799fac8e67bfa3147"));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }
    @RequestMapping(value = "/get/access/token/{shopId}/test",method = RequestMethod.GET)
    public ResponseEntity getTokenAppOrderTest(@PathVariable long shopId ,@RequestParam long shop_id,@RequestParam String code){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",shopeeService.getAccess_token_Test(shopInfo.get(),code,shop_id,"7c31e0d6f76dbadd99aa848371d6e5a3b97b00dae2d30b4799fac8e67bfa3147",1001118L,2L));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }
    @RequestMapping(value = "/get/ship/test",method = RequestMethod.GET)
    public ResponseEntity getShipTest(@RequestParam String order,@RequestParam Long shopeeId){
        return new ResponseEntity(orderServices.shipOrder(order,shopeeId),HttpStatus.OK);
    }

}
