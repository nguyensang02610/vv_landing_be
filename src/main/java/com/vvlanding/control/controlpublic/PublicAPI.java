package com.vvlanding.control.controlpublic;


import com.vvlanding.dto.*;
import com.vvlanding.repo.RepoConfig;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.service.*;
import com.vvlanding.storage.StorageService;
import com.vvlanding.table.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("api/public")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicAPI {

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    OrderService orderService;

    @Autowired
    SerProduct serProduct;

    @Autowired
    SerProductVariations serProductVariations;

    @Autowired
    SerReview serReview;

    @Autowired
    SerBill serBill;

    @Autowired
    SerGhtkService shippedService;
    private StorageService storageService;

    @Autowired
    SerLandingPage serLandingPage;

    @Autowired
    SerTitleLading serTitleLading;

    @Autowired
    SerBannerLading serBannerLading;

    @Autowired
    SerRefLandingPageUser serRefLandingPageUser;

    @Autowired
    SerConfig serConfig;

    @Autowired
    RepoConfig repoConfig;

    @Autowired
    SerTitleLandingPage serTitleLandingPage;

    @Autowired
    private RabbitTemplate template;

//    Long - QUEUE
//    @RequestMapping(value = "/landingpage/create/order/rabbit", method = RequestMethod.POST)
//    @CrossOrigin(origins = "*", maxAge = 3600)
//    public ResponseEntity<?> InsOrderRabbit(@RequestBody DtoOrder prInput, @RequestParam(name = "shop_token") String shopToken) {
//
//        Map<String, Object> response = new HashMap<>();
//        try {
//            Optional<ShopInfo> shopOpt = serShopInfo.findByShopToken(shopToken);
//            if (!shopOpt.isPresent()) {
//                response.put("message", "không tìm thấy thông tin shop !!!!");
//                response.put("success", false);
//                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//            }
//            System.out.println("prInput---" + prInput.getWeight());
//            template.convertAndSend(MQConfig.EXCHANGE,
//                    MQConfig.ROUTING_KEY, prInput);
//            response.put("message", "Thêm đơn thành công");
//            response.put("success", true);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("error", e.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//    }

//    Cong - ins order landing
    @RequestMapping(value = "/landingpage/create/order", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> InsOrder(@RequestBody DtoOrder prInput, @RequestParam(name = "shop_token") String shopToken) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ShopInfo> shopOpt = serShopInfo.findByShopToken(shopToken);
            if (!shopOpt.isPresent()) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(orderService.InsSent(prInput), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    //Cong - get product landing
    @RequestMapping(value = "/getProduct", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> GetProduct(@RequestParam long productID, @RequestParam(name = "shop_token") String shopToken) {
        Optional<ShopInfo> shopOpt = serShopInfo.findByShopToken(shopToken);
        if (!shopOpt.isPresent()) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop",
                    null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        }
        Optional<Product> productOpt = serProduct.findByShopToken(productID, shopToken);
        if (productOpt.isPresent()) {
            List<ProductVariations> variations = serProductVariations.findByProduct(productOpt.get());
            DtoProduct producVariants2Json = serProduct.producVariants2Json(shopOpt.get(), productOpt.get(),
                    variations);
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", producVariants2Json);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
        }
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "failed",
                "Không tìm thấy thông tin sản phẩm");
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

    //Sang - get All review
    @RequestMapping(value = "/getReview", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getReview(@RequestParam long shopId) {
        List<DtoReview> dtoReviews = serReview.getAll(shopId);
        if (dtoReviews.isEmpty()) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop", null);
            return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.BAD_REQUEST);
        }
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", dtoReviews);
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

    //Tạo review
    @RequestMapping(value = "/create/review", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> insertReview(@RequestBody DtoReview dtoReview, @RequestParam long shopId) {
        dtoReview.setShopId(shopId);
        serReview.insert(dtoReview);
        return new ResponseEntity<ResponseStatusMessage>(HttpStatus.OK);
    }

    //sửa review
    @RequestMapping(value = "/update/review", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateReview(@RequestBody DtoReview dtoReview, @RequestParam long id, long shopId) {
        dtoReview.setShopId(shopId);
        dtoReview.setId(id);
        serReview.update(dtoReview);
        return new ResponseEntity<ResponseStatusMessage>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/review", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> deleteReview(@RequestParam long id) {
        serReview.delete(id);
        return new ResponseEntity<ResponseStatusMessage>(HttpStatus.OK);
    }

    //Lấy 10 bill gần nhất
    @RequestMapping(value = "/top/bill", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getTopBill(@RequestParam long shopId) {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serBill.getTopBill(shopId));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/top/date", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> findBillByDate(@RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serBill.findBillByDate(fromDate, toDate));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

    //upload ảnh banner
    @PostMapping(value = "landingpage/image/{shopID}")
    public ResponseEntity<?> image(@RequestParam("file") MultipartFile[] files, @PathVariable long shopID) {
        List<String> storedFilePaths = new ArrayList<String>();
        System.out.println(files);
        for (MultipartFile file : files) {
            System.out.println(file.getName());
        }
        try {
            for (MultipartFile file : files) {
                String storedFilePath = storageService.themeImage(file, shopID);
                storedFilePaths.add(storedFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseStatusMessage(false, "Cannot store this file", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseStatusMessage(true, "Upload file successfully", storedFilePaths),
                HttpStatus.ACCEPTED);
    }

    //ok
    @GetMapping("shop/{shopID}/getRefById")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> showRefById(@PathVariable long shopID, @RequestParam long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<ShopInfo> shopOpt = serShopInfo.FindById2(shopID);
        ShopInfo shop = shopOpt.get();
        if (shopOpt.isPresent()) {
            try {
                RefLandingPageUser refLandingPageUser = serRefLandingPageUser.getRefLandingPageUserId(id, shopID);
                List<TitleLanding> titleLandings = serTitleLading.findByRef(refLandingPageUser);
                List<BannerLanding> bannerLandings = serBannerLading.findByRef(refLandingPageUser);
                List<TitleLandingPage> titleLandingPages = serTitleLandingPage.findByRef(refLandingPageUser);
                DtoRefLandingPageUser dtoRefLandingPageUser = serRefLandingPageUser.GetAllRefLandingPageUserss(shop, refLandingPageUser, bannerLandings,
                        titleLandings, titleLandingPages);
                response.put("data", dtoRefLandingPageUser);
                response.put("success", true);
                response.put("message", "Ok");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception e) {
                response.put("success", false);
                response.put("error", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        response.put("message", "không tìm thấy thông tin shop !!!!");
        response.put("success", false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("ref/{RefID}/getConfig")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> showConfigById(@PathVariable long RefID) {
        Map<String, Object> response = new HashMap<>();
        Optional<RefLandingPageUser> refOpt = serRefLandingPageUser.find(RefID);
        RefLandingPageUser refLandingPageUser = refOpt.get();
        if (refOpt.isPresent()) {
            List<Config> config = serConfig.findByRefLandingPageUser(refLandingPageUser);
            for (Config c : config) {
                DtoConfig dtoConfig = serConfig.GetAllConfig(refLandingPageUser, c);
                response.put("data", dtoConfig);
                response.put("success", true);
                response.put("message", "Ok");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        response.put("message", "không tìm thấy thông tin Ref !!!!");
        response.put("success", false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "get/bill/status", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getBillByStatus(@RequestParam long shopId, @RequestParam long statusId) {
        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serBill.getAllBillByShopId(shopId, statusId));
        return new ResponseEntity<ResponseStatusMessage>(statusMessage, HttpStatus.OK);
    }

//    @RequestMapping(value = "get/update/status",method = RequestMethod.GET)
//    @CrossOrigin(origins = "*",maxAge = 3600)
//    public ResponseEntity<?> updateStatus(@RequestParam long shopId,@RequestParam long id){
//        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serBill.updateStatusById(shopId,id));
//        return new ResponseEntity<ResponseStatusMessage>(statusMessage,HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "update/list/status",method = RequestMethod.GET)
//    @CrossOrigin(origins = "*",maxAge = 3600)
//    public ResponseEntity<?> updateListStatus(@RequestParam long shopId,@RequestParam List<Long> id){
//        ResponseStatusMessage statusMessage = new ResponseStatusMessage(true,"success",serBill.updateListStatus(shopId,id));
//        return new ResponseEntity<ResponseStatusMessage>(statusMessage,HttpStatus.OK);
//    }

}


