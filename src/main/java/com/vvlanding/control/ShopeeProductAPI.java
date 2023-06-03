package com.vvlanding.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vvlanding.repo.RepoChannelShopee;
import com.vvlanding.repo.RepoProduct;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.shopee.item.v2.addProduct.RequestProduct;
import com.vvlanding.shopee.service.shopee.ItemShopeeService;
import com.vvlanding.shopee.service.shopee.ShopAuthServices;
import com.vvlanding.shopee.service.shopee.ShopeeService;
import com.vvlanding.table.ChannelShopee;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/shopee/product")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ShopeeProductAPI {

    private String domain = Iconstants.domain;

    private final String partnerProductKey = Iconstants.test_key_product_v2;

    private final long partnerProductId = 2001408;

    @Autowired
    ShopeeService shopeeService;

    @Autowired
    ItemShopeeService itemShopeeService;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoChannelShopee repoChannelShopee;

    @Autowired
    RepoProduct repoProduct;

    @RequestMapping(value = "/get/shop/{shopID}",method = RequestMethod.GET)
    public ResponseEntity getShopAppProduct(@CurrentUser UserPrincipal user, @PathVariable long shopID, @RequestParam String code, @RequestParam(name = "shop_id") long shopeeShopId) throws JsonProcessingException {
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopID);
        Map<String,Object> response = new HashMap<>();
        if (!shopInfo.isPresent()){
            response.put("message","không tìm thấy thông tin shop");
            response.put("success",false);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeShopId);
            if (channelShopee.isPresent() && channelShopee.get().getShop().getId() != shopID){
                response.put("message","tài khoản shopee đã đăng ký ở landing khác");
                response.put("success",false);
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
            shopeeService.getAccess_token(shopInfo.get(),code,shopeeShopId,partnerProductKey,partnerProductId,1L);
            String access_token = shopeeService.getToken(shopeeShopId,partnerProductKey,partnerProductId,1L);
            ResponseEntity responseEntity = shopeeService.getShopInfoV2(shopInfo.get(),shopeeShopId,partnerProductKey,partnerProductId,1L,access_token);
            itemShopeeService.fetchProductV2(user,20,shopeeShopId,shopID,access_token);
            return responseEntity;
        }catch (Exception e){
            e.printStackTrace();
            response.put("success",false);
            response.put("message",e.getMessage());
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/get/{shopId}/list/item/v2",method = RequestMethod.GET)
    public ResponseEntity getListItemV2(@CurrentUser UserPrincipal currentUser,@RequestParam int pageNumber,@PathVariable long shopId, @RequestParam(name = "shop_id") List<Long> shopeeShopId) throws JsonProcessingException {
        Map<String,Object> response = new HashMap<>();
        for (Long l:shopeeShopId) {
            String access_token = shopeeService.getToken(l, partnerProductKey, partnerProductId, 1L);
            System.out.println(access_token);
            if (access_token.equals("invalid")){
                ChannelShopee channelShopee = shopeeService.getShopeeById(l);
                if (channelShopee != null) response.put("message", "token shopee " + channelShopee.getShopeeShopName() + " đã hết hạn");
                response.put("success", false);
                response.put("data",null);
                return ResponseEntity.badRequest().body(response);
            }
            itemShopeeService.fetchProductV2(currentUser,pageNumber,l,shopId,access_token);
        }
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",null);
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }

    @GetMapping(value = "/cancel/auth")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity cancelAppProductV2() {
        String shopeeRedirectUrl = domain + "/shopee";
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success", ShopAuthServices.cancelAppV2(shopeeRedirectUrl,partnerProductKey,partnerProductId));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }


    // get url kết nối app order
    @GetMapping(value = "/get/url/v2")
    public ResponseEntity getAuthAppProduct() {
        String shopeeRedirectUrl = domain + "/shopee";
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",ShopAuthServices.authShopV2(partnerProductId,shopeeRedirectUrl,partnerProductKey));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }

    @RequestMapping(value = "/get/access/token/{shopId}",method = RequestMethod.GET)
    public ResponseEntity getTokenAppProduct(@PathVariable long shopId ,@RequestParam long shop_id,@RequestParam String code){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",shopeeService.getAccess_token(shopInfo.get(),code,shop_id,partnerProductKey,partnerProductId,1L));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }
    @GetMapping(value ="/get/logistic")
    public ResponseEntity<?> getLogistic(@RequestParam long shopeeId){
        return itemShopeeService.getLogistic(shopeeId,Iconstants.partner_key_order_v2,Iconstants.partner_id_order_v2);
    }

    @GetMapping(value = "/get/category")
    public ResponseEntity<?> getCategory(@RequestParam Long shopeeId){
        return itemShopeeService.getCategory(shopeeId,partnerProductKey,partnerProductId);
    }

    @GetMapping(value = "/get/category/name")
    public ResponseEntity<?> getCategory(@RequestParam String name){
        return itemShopeeService.getCategoryByName(name);
    }
    @GetMapping(value = "/get/attributes")
    public ResponseEntity<?> getAttributes(@RequestParam Long shopeeId,@RequestParam Long categoryId ){
        return itemShopeeService.getAttributes(categoryId,shopeeId,partnerProductKey,partnerProductId);
    }
    @GetMapping(value = "/get/brand")
    public ResponseEntity<?> getBrand(@RequestParam Long shopeeId,@RequestParam Long categoryId ,@RequestParam Integer offset){
        return itemShopeeService.getBrand(shopeeId,offset,categoryId,partnerProductKey,partnerProductId);
    }
    @PostMapping(value = "/add/product")
    public ResponseEntity<?> upProductToShopee(@RequestParam Long productId,@RequestParam Long shopeeId,@RequestParam Long categoryId,@RequestParam Long logisticId){
        RequestProduct requestProduct = new RequestProduct();
        requestProduct.setProductId(productId);
        requestProduct.setCategoryId(categoryId);
        requestProduct.setLogisticId(logisticId);
        requestProduct.setQuantity(10);
        return itemShopeeService.upProductToShopee(requestProduct,shopeeId,partnerProductKey,partnerProductId);
    }

    @PostMapping(value = "/up/image")
    public ResponseEntity<?> upImage(@RequestParam Long productId,@RequestParam Long shopeeId){
        return itemShopeeService.uploadImgTEST(shopeeId,productId,partnerProductKey,partnerProductId);
    }


    //*
    // Test......................
    // */
    @GetMapping(value = "/get/url/v2/test")
    public ResponseEntity getAuthAppOrderTest() {
        String shopeeRedirectUrl = "http://localhost:6868/bill";
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",ShopAuthServices.authShopV2Test(1001814,shopeeRedirectUrl,"a1d470e6eb92d3db712f69a0f5fa68d121060db83dfb665dc73a4bee72fa7adc"));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }
    @RequestMapping(value = "/get/access/token/{shopId}/test",method = RequestMethod.GET)
    public ResponseEntity getTokenAppOrderTest(@PathVariable long shopId ,@RequestParam long shop_id,@RequestParam String code){
        Optional<ShopInfo> shopInfo =repoShopInfo.findById(shopId);
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",shopeeService.getAccess_token_Test(shopInfo.get(),code,shop_id,"a1d470e6eb92d3db712f69a0f5fa68d121060db83dfb665dc73a4bee72fa7adc",1001814L,1L));
        return new ResponseEntity(statusMessage,HttpStatus.OK);
    }

    @GetMapping(value = "/test/test2")
    public ResponseEntity<?> testttt(@RequestParam String file) throws IOException {
        return ResponseEntity.ok(itemShopeeService.test(file));
    }

}
