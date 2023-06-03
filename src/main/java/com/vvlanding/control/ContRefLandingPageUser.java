package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoRefLandingPageUser;
import com.vvlanding.payload.ResponseLandUser;
import com.vvlanding.payload.ResponseStatus;
import com.vvlanding.repo.RepoRefLandingPageUser;
import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.*;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/reflanding")

public class ContRefLandingPageUser {
    @Autowired
    SerRefLandingPageUser serRefLandingPageUser;

    @Autowired
    RepoRefLandingPageUser repoRefLandingPageUser;

    @Autowired
    SerUser serUser;

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    SerLandingPage serLandingPage;

    @Autowired
    SerTitleLading serTitleLading;

    @Autowired
    SerBannerLading serBannerLading;

    @Autowired
    SerTitleLandingPage serTitleLandingPage;

    //ok
    //API Lấy RefLanding của shop theo id shop, tìm kiếm và phân trang
    @GetMapping(value = {"shop/{shopID}"})
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> productManagent1(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID, Pageable pageable, String query) {
        Map<String, Object> response = new HashMap<>();
        int start;
        int end;
        List<DtoRefLandingPageUser> listDtoRefLandingPageUser = new ArrayList<DtoRefLandingPageUser>();
        Resp resp = new Resp();
        try {
            ShopInfo opShop = serShopInfo.FindById(shopID, currentUser);
            if (opShop == null) {
                response.put("mesager", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            boolean b = query == null || query.equals(null);
            if (pageable.getPageNumber() == 0) {
                if (b) {
                    start = (int) PageRequest.of(0, 10).getOffset();
                    listDtoRefLandingPageUser = serRefLandingPageUser.getRefLandingOfShop(opShop);
                    end = (start + 10) > listDtoRefLandingPageUser.size() ? listDtoRefLandingPageUser.size() : 10;
                    resp.setMetaRepo(1, 10, listDtoRefLandingPageUser.size());
                    listDtoRefLandingPageUser = listDtoRefLandingPageUser.subList(start, end);
                } else {
                    listDtoRefLandingPageUser = serRefLandingPageUser.findRefLandingPageUserByQuery(shopID, query, query);
                    if (listDtoRefLandingPageUser.size() > 0) {
                        start = (int) PageRequest.of(0, 10).getOffset();
                        end = (start + 10) > listDtoRefLandingPageUser.size() ? listDtoRefLandingPageUser.size() : 10;
                        resp.setMetaRepo(1, 10, listDtoRefLandingPageUser.size());
                        listDtoRefLandingPageUser = listDtoRefLandingPageUser.subList(start, end);
                    } else {
                        response.put("success", "false");
                        response.put("message", "Không tìm thấy sản phẩm");
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                }
            } else {
                if (b) {
                    listDtoRefLandingPageUser = serRefLandingPageUser.getRefLandingOfShop(opShop);
                    start = (int) PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()).getOffset();
                    end = Math.min((start + pageable.getPageSize()), listDtoRefLandingPageUser.size());
                    resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), listDtoRefLandingPageUser.size());
                } else {
                    listDtoRefLandingPageUser = serRefLandingPageUser.findRefLandingPageUserByQuery(shopID, query, query);
                    if (listDtoRefLandingPageUser.size() > 15) {
                        start = (int) PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()).getOffset();
                        end = Math.min((start + pageable.getPageSize()), listDtoRefLandingPageUser.size());
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), listDtoRefLandingPageUser.size());
                    } else {
                        start = (int) PageRequest.of(0, 10).getOffset();
                        end = (start + 10) > listDtoRefLandingPageUser.size() ? listDtoRefLandingPageUser.size() : 10;
                        resp.setMetaRepo(1, 10, listDtoRefLandingPageUser.size());
                    }
                }
                listDtoRefLandingPageUser = listDtoRefLandingPageUser.subList(start, end);
            }
            response.put("data", listDtoRefLandingPageUser);
            response.put("meta", resp.getMeta());
            response.put("success", true);
            response.put("message", "Ok");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    // update domain
    @RequestMapping(value = "shop/{shopID}/upd", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateSent(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID, @RequestBody DtoRefLandingPageUser prInput) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serRefLandingPageUser.checkShop(shopID, currentUser);
            if (!checkShop) {
                response.put("mesager", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            return serRefLandingPageUser.upDate(prInput, shopID);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    @RequestMapping(value = "/ins", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> InsShop(@CurrentUser UserPrincipal currentUser, @RequestBody ResponseLandUser prInput) {
        Object data = serRefLandingPageUser.insSer(prInput, currentUser);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // api VVECO - kich hoat domain
    @RequestMapping(value = "/shop/{shopId}/statusDomain", method = RequestMethod.POST)
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
                return serRefLandingPageUser.updateStatus(shopInfo, responseStatus);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //ok
//    // @author cong - domain by id  status === false
//    @RequestMapping(value = "shop/{shopID}/del", method = RequestMethod.POST)
//    @CrossOrigin(origins = "*", maxAge = 3600)
//    public ResponseEntity<Map<String, Object>> delete(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID, @RequestParam Long id) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            boolean checkShop = serRefLandingPageUser.checkShop(shopID, currentUser);
//            if (!checkShop) {
//                response.put("mesager", "không tìm thấy thông tin shop !!!!");
//                response.put("success", false);
//                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//            }
//            RefLandingPageUser refLandingPageUser = serRefLandingPageUser.checkRef(id, shopID);
//            if (refLandingPageUser != null) {
//                refLandingPageUser.setStatus(false);
//                RefLandingPageUser data = repoRefLandingPageUser.save(refLandingPageUser);
//                response.put("data", data);
//                response.put("mesager", "tắt domain");
//                response.put("success", true);
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            } else {
//                response.put("mesager", "không tìm thấy thông tin domain  !!!!");
//                response.put("success", false);
//                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("error", e.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    //ok
    //- Thêm Title vào theme
    @RequestMapping(value = "/insThemeTitle", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> insTitle(@CurrentUser UserPrincipal currentUser, @RequestParam long shopID, @RequestBody DtoRefLandingPageUser prInput) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
            if (!checkShop) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return serRefLandingPageUser.insSerThemeTitleLanding(prInput, shopID);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    //ok
    // edit Title Theme
    @RequestMapping(value = "/editThemeTitle", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateTitle(@CurrentUser UserPrincipal currentUser, @RequestParam long shopID, @RequestBody DtoRefLandingPageUser prInput) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
            if (!checkShop) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return serRefLandingPageUser.updateLandingPageTitle2(prInput, shopID);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    //- Thêm Banner vào theme
    @RequestMapping(value = "/insThemeBanner", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> insBanner(@CurrentUser UserPrincipal currentUser, @RequestParam long shopID, @RequestBody DtoRefLandingPageUser prInput) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
            if (!checkShop) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return serRefLandingPageUser.insSerThemeBanner(prInput, shopID);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    // edit Banner Theme
    @RequestMapping(value = "/editThemeBanner", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> updateBanner(@CurrentUser UserPrincipal currentUser, @RequestParam long shopID, @RequestBody DtoRefLandingPageUser prInput) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
            if (!checkShop) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return serRefLandingPageUser.updateLandingPageBanner(prInput, shopID);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    @GetMapping("/{idRef}/getBannerByIdRef")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> showRefById(@CurrentUser UserPrincipal currentUser, @RequestParam long shopID, @PathVariable long idRef) {
        Map<String, Object> response = new HashMap<>();
        ShopInfo shopOpt = serShopInfo.FindById(shopID, currentUser);
        if (shopOpt != null) {
            try {
                RefLandingPageUser refLandingPageUser = serRefLandingPageUser.getRefLandingPageUserId(idRef, shopID);
                List<BannerLanding> bannerLandings = serBannerLading.findByRef(refLandingPageUser);
                DtoRefLandingPageUser dtoRefLandingPageUser = serBannerLading.GetAllBannerByRefId(refLandingPageUser, bannerLandings, shopOpt);
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

    //ok
    @DeleteMapping("{RefID}/delete")
    public ResponseEntity<?> deleteBanner(@CurrentUser UserPrincipal currentUser, @RequestParam long shopID, @PathVariable long RefID) {
        ShopInfo shopOpt = serShopInfo.FindById(shopID, currentUser);
        if (shopOpt != null) {
            try {
                serBannerLading.deleteBanner(RefID, shopOpt);
                return new ResponseEntity<ResponseStatusMessage>(
                        new ResponseStatusMessage(true, "success", "Xóa Banner thành công"), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(new ResponseStatusMessage(false, "error", "Không xóa đc Banner của Ref " + RefID),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<ResponseStatusMessage>(
                new ResponseStatusMessage(true, "success", "Không thấy Shop"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/update/time", method = RequestMethod.POST)
    public ResponseEntity<?> updateTimeLanding(@RequestParam long id, @RequestParam long time) {
        ResponseStatusMessage message = new ResponseStatusMessage(true, "success", serRefLandingPageUser.updateTime(id, time));
        return new ResponseEntity<ResponseStatusMessage>(message, HttpStatus.OK);
    }

    //ok
    @RequestMapping(value = "/delete/domain/{shopId}" ,method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*",maxAge = 3600)
    public ResponseEntity deleteBill(@CurrentUser UserPrincipal currentUser,@PathVariable long shopId , @RequestParam long refId){
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopId, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(false, "Không tìm thấy thông tin shop",
                    null);
            return new ResponseEntity(statusMessage, HttpStatus.BAD_REQUEST);
        }else {
            ResponseStatusMessage statusMessage = new ResponseStatusMessage(true, "success", serRefLandingPageUser.deleteDomain(refId, shopId));
            return new ResponseEntity(statusMessage, HttpStatus.OK);
        }
    }

}
