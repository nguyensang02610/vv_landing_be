package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.dto.DtoProduct;
import com.vvlanding.dto.DtoProductVariations;
import com.vvlanding.mapper.MapperProduct;
import com.vvlanding.mapper.MapperProductVariations;
import com.vvlanding.repo.RepoProduct;
import com.vvlanding.repo.RepoProductVariations;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.repo.RepoShopUserRole;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerProduct;
import com.vvlanding.service.SerProductVariations;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.table.Product;
import com.vvlanding.table.ProductVariations;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.table.ShopUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("api/shop/{shopID}/product")
public class ProductController {

    @Autowired
    SerProduct serProduct;

    @Autowired
    MapperProduct mapperProduct;

    @Autowired
    RepoProduct repoProduct;

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    RepoShopUserRole repoShopUserRole;

    @Autowired
    SerProductVariations serProductVariations;

    @Autowired
    RepoProductVariations repoProductVariations;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    MapperProductVariations mapperProductVariations;

    //ok
    //thecong
    @GetMapping(value = {""})
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getProduct(@CurrentUser UserPrincipal currentUser, @PathVariable Long shopID, String query, Pageable pageable) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopID, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            try {
                int start;
                int end;
                List<DtoProduct> listDtoProduct;
                Resp resp = new Resp();
                Pageable pageable1;
                int total = 0;
                boolean b = query == null || query.equals(null);
                if (pageable.getPageNumber() == 0 || pageable.getPageNumber() == 1) {
                    if (b) {
                        start = (int) PageRequest.of(0, 10).getOffset();
                        listDtoProduct = serProduct.getProductOfShopId(shopInfo);
                        total = listDtoProduct.size();
                        end = (start + 10) > listDtoProduct.size() ? listDtoProduct.size() : 10;
                        resp.setMetaRepo(1, 10, listDtoProduct.size());
                        listDtoProduct = listDtoProduct.subList(start, end);
                    } else {
                        listDtoProduct = serProduct.findProductByQuery(shopInfo, query, query);
                        total = listDtoProduct.size();
                        start = (int) PageRequest.of(0, 10).getOffset();
                        end = (start + 10) > listDtoProduct.size() ? listDtoProduct.size() : 10;
                        resp.setMetaRepo(1, 10, listDtoProduct.size());
                        listDtoProduct = listDtoProduct.subList(start, end);
                    }
                    resp.setMetaRepo(1, 10, total);
                    repose.put("data", listDtoProduct);
                } else {
                    pageable1 = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
                    if (b) {
                        listDtoProduct = serProduct.getProductOfShopPage(shopInfo, pageable1);
                        total = serProduct.getProductOfShopId(shopInfo).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoProduct);
                    } else {
                        listDtoProduct = serProduct.findProductByQueryPage(shopInfo, query, query, pageable1);
                        total = serProduct.findProductByQuery(shopInfo, query, query).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoProduct);
                    }
                }
                repose.put("meta", resp.getMeta());
                repose.put("success", true);
                repose.put("message", "Ok");
                return new ResponseEntity<>(repose, HttpStatus.OK);
            } catch (Exception e) {
                repose.put("success", false);
                repose.put("error", e.getMessage());
                return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
            }
        }
    }


    @GetMapping(value = {"/chat"})
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> getProductChat(@CurrentUser UserPrincipal currentUser, @PathVariable Long shopID, String query, Pageable pageable) {
        Map<String, Object> repose = new HashMap<>();
        ShopInfo shopInfo = serShopInfo.checkShopFindId(shopID, currentUser);
        if (shopInfo == null || shopInfo.equals(null)) {
            repose.put("message", "Không tìm thấy thông tin shop");
            repose.put("success", "false");
            return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
        } else {
            try {
                int start;
                int end;
                List<DtoProduct> listDtoProduct;
                Resp resp = new Resp();
                Pageable pageable1;
                int total = 0;
                boolean b = query == null || query.equals(null);
                if (pageable.getPageNumber() == 0 || pageable.getPageNumber() == 1) {
                    if (b) {
                        start = (int) PageRequest.of(0, 5).getOffset();
                        listDtoProduct = serProduct.getProductOfShopId(shopInfo);
                        total = listDtoProduct.size();
                        end = (start + 5) > listDtoProduct.size() ? listDtoProduct.size() : 5;
                        resp.setMetaRepo(1, 5, listDtoProduct.size());
                        listDtoProduct = listDtoProduct.subList(start, end);
                    } else {
                        listDtoProduct = serProduct.findProductByQuery(shopInfo, query, query);
                        total = listDtoProduct.size();
                        start = (int) PageRequest.of(0, 5).getOffset();
                        end = (start + 5) > listDtoProduct.size() ? listDtoProduct.size() : 5;
                        resp.setMetaRepo(1, 5, listDtoProduct.size());
                        listDtoProduct = listDtoProduct.subList(start, end);
                    }
                    resp.setMetaRepo(1, 5, total);
                    repose.put("data", listDtoProduct);
                } else {
                    pageable1 = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
                    if (b) {
                        listDtoProduct = serProduct.getProductOfShopPage(shopInfo, pageable1);
                        total = serProduct.getProductOfShopId(shopInfo).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoProduct);
                    } else {
                        listDtoProduct = serProduct.findProductByQueryPage(shopInfo, query, query, pageable1);
                        total = serProduct.findProductByQuery(shopInfo, query, query).size();
                        resp.setMetaRepo(pageable.getPageNumber(), pageable.getPageSize(), total);
                        repose.put("data", listDtoProduct);
                    }
                }
                repose.put("meta", resp.getMeta());
                repose.put("success", true);
                repose.put("message", "Ok");
                return new ResponseEntity<>(repose, HttpStatus.OK);
            } catch (Exception e) {
                repose.put("success", false);
                repose.put("error", e.getMessage());
                return new ResponseEntity<>(repose, HttpStatus.BAD_REQUEST);
            }
        }
    }


    //ok
    // @author hungpham get product by sku
    @RequestMapping(value = "/find/{sku}", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> findById(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID, @PathVariable String sku) {
        Map<String, Object> response = new HashMap<>();
        boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
        if (checkShop) {
            try {
                Product data = serProduct.getProductBySku(sku, shopID);
                List<ProductVariations> productVariants = serProductVariations.findByProduct(data);
                DtoProduct dtoProduct = serProduct.producVariants2Json(data.getShopInfo(), data, productVariants);
                response.put("data", dtoProduct);
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

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> findProductVari(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID) {
        Map<String, Object> response = new HashMap<>();
        boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
        if (checkShop) {
            try {
                Optional<ShopUserRole> shopUserRoleOptional = repoShopUserRole.findByShopInfoIdAndUserId(shopID, currentUser.getId());
                ShopInfo shopInfo = shopUserRoleOptional.get().getShopInfo();
                List<ProductVariations> data = repoProductVariations.findByProductsShopInfoAndProductsIsActive(shopInfo, true);
                List<DtoProductVariations> dtoProductVariations = new ArrayList<>();
                for (ProductVariations productVariations1 : data) {
                    DtoProductVariations dtoProductVariations1 = mapperProductVariations.toDto(productVariations1);
                    dtoProductVariations1.setProductId(productVariations1.getProducts().getId());
                    dtoProductVariations1.setProductSku(productVariations1.getProducts().getSku());
                    dtoProductVariations1.setProductName(productVariations1.getProducts().getTitle());
                    dtoProductVariations1.setChannel(productVariations1.getProducts().getChannel());
                    dtoProductVariations.add(dtoProductVariations1);
                }
                response.put("data", dtoProductVariations);
                response.put("size", dtoProductVariations.size());
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
    // update product
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> update(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID, @RequestBody DtoProduct prInput) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
            if (!checkShop) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return serProduct.updateProduct(prInput, shopID);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

//        ok
//     @author hungpham delete product by id  isactive === false
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<Map<String, Object>> delete(@CurrentUser UserPrincipal currentUser, @PathVariable long shopID, @RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checkShop = serShopInfo.checkShop(shopID, currentUser);
            if (!checkShop) {
                response.put("message", "không tìm thấy thông tin shop !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Product product = serProduct.checkProduct(id, shopID);
            if (!product.equals(null)) {
                product.setIsActive(false);
                repoProduct.save(product);
                List<ProductVariations> listProductVariation = repoProductVariations.findAll();
                for (ProductVariations item : listProductVariation) {
                    if (item.getId().equals(product.getId())) {
                        item.setIsActive(false);
                        repoProductVariations.save(item);
                    }
                }
                response.put("message", "xóa thông tin sản phẩm");
                response.put("success", true);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "không tìm thấy thông tin sản  !!!!");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
