package com.vvlanding.shopee.service.shopee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.dto.DtoProduct;
import com.vvlanding.dto.DtoProductVariations;
import com.vvlanding.repo.*;
import com.vvlanding.shopee.Common;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.shopee.chat.ResponseImg;
import com.vvlanding.shopee.item.*;
import com.vvlanding.shopee.item.v2.*;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerProduct;
import com.vvlanding.shopee.item.v2.addProduct.*;
import com.vvlanding.storage.StorageService;
import com.vvlanding.table.*;
import com.vvlanding.table.Properties;
import com.vvlanding.utils.Constant;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ItemShopeeService {

    @Autowired
    private SerProduct serProduct;

    @Autowired
    private StorageService storageService;

//    @Autowired
//    private RepoShopeeCategory repoShopeeCategory;

    private long partnerID = Iconstants.partner_id;

    private String partnerKey = Iconstants.partner_key;

    private long partnerIDV2 = Iconstants.partner_id_product_v2;

    private String partnerKeyV2 = Iconstants.test_key_product_v2;

    private static final long partnerIdTest = 1001814;
    private static final String partnerKeyTest = "a1d470e6eb92d3db712f69a0f5fa68d121060db83dfb665dc73a4bee72fa7adc";

    //Auto Generate Code./
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    private ShopeeService shopeeService;

    @Autowired
    RepoProductVariations repoProductVariations;

    @Autowired
    RepoProductShopee repoProductShopee;

    @Autowired
    RepoChannelShopee repoChannelShopee;

    @Autowired
    RepoCategoryShopee repoCategoryShopee;

    @Autowired
    RepoProduct repoProduct;

    public void fetchProduct(UserPrincipal currentUser, Long shopeeId, Long shopId) throws JsonProcessingException {
        int pageNumber = 0;
        while (getListItem(currentUser, pageNumber, shopeeId, shopId)) {
            pageNumber += 100;
        }
    }

    public void fetchProductV2(UserPrincipal currentUser,int pageNumber, Long shopeeId, Long shopId, String access_token) {
        getListItemV2(currentUser, pageNumber, shopeeId, shopId, access_token);
    }

    public boolean getListItemV2(UserPrincipal currentUser, int pageNumber, Long shopeeId, Long shopId, String access_token) {
        ObjectMapper mapper = new ObjectMapper();
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/product/get_item_list";
        String url = "?offset=" + pageNumber + "&page_size=10" + "&item_status=NORMAL";
        int cnt = 0;
        ItemResponseDTO itemResponseDTO = null;
        while (itemResponseDTO == null && cnt < 5) {
            cnt++;
            try {
                itemResponseDTO = Common.callAPIV2(url, host, path, access_token, String.valueOf(shopeeId), partnerKeyV2, String.valueOf(partnerIDV2), null, ItemResponseDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
                itemResponseDTO = null;
            }
        }
        if (itemResponseDTO != null && itemResponseDTO.getResponse().getItemV2() != null) {
            String path_detail = "/api/v2/product/get_item_base_info";
            String urls = "?item_id_list=";
            String item = "?item_id=";
            String path_model = "/api/v2/product/get_model_list";
            String str = "";
            for (ItemV2 i : itemResponseDTO.getResponse().getItemV2()) {
                str = str + i.getItem_id() + ",";
            }
            urls = urls + str.substring(0, str.length() - 1);
            ItemDetailResponseDTO itemDetailsResponseDTO = null;
            cnt = 0;
            while (itemDetailsResponseDTO == null && cnt < 5) {
                cnt++;
                try {
                    itemDetailsResponseDTO = Common.callAPIV2(urls, host, path_detail, access_token, String.valueOf(shopeeId)
                            , partnerKeyV2, String.valueOf(partnerIDV2), null, ItemDetailResponseDTO.class);
                } catch (Exception e) {
                    itemDetailsResponseDTO = null;
                }
            }
            for (ItemDetail c : itemDetailsResponseDTO.getResponse().getItem_list()) {
                String string = item + c.getItem_id();
                ModelResponseDTO modelResponseDTO = null;
                int a = 0;
                while (modelResponseDTO == null && a < 5) {
                    a++;
                    try {
                        Thread.sleep(500);
                        modelResponseDTO = Common.callAPIV2(string, host, path_model, access_token, String.valueOf(shopeeId)
                                , partnerKeyV2, String.valueOf(partnerIDV2), null, ModelResponseDTO.class);
                    } catch (Exception e) {
                        modelResponseDTO = null;
                    }
                }
                List<Properties> properties = new ArrayList<>();
                List<TierVariation> tierVariation = new ArrayList<>();
                List<ModelItem> modelItems = new ArrayList<>();
                List<DtoProductVariations> dtoProductVariations = new ArrayList<>();
                if (modelResponseDTO != null) {
                    tierVariation = modelResponseDTO.getResponse().getTier_variation();
                    modelItems = modelResponseDTO.getResponse().getModel();
                    ObjectMapper mapper1 = new ObjectMapper();
                    try {
                        System.out.println(mapper1.writeValueAsString(modelItems));
                    }catch (Exception e){

                    }

                }
                DtoProduct dtoProduct = new DtoProduct();
                dtoProduct.setIsActive(true);
                if (c.getItem_sku() != null) {
                    dtoProduct.setSku("shopee" + "-" + c.getItem_sku() + "-" + getRandomNumber(3));
                } else dtoProduct.setSku("shopee" + "-" + getRandomNumber(8));
                dtoProduct.setTitle(c.getItem_name());
                int mo = 0;
                if (tierVariation.size() > 1) {
                    for (int j = 0; j < tierVariation.get(0).getOption_list().size(); j++) {
                        Properties properties1 = new Properties();
                        properties1.setKeyname(tierVariation.get(0).getName());
                        properties1.setValue(tierVariation.get(0).getOption_list().get(j).getOption());
                        properties1.setCreatedDate(new Date());
                        properties1.setModifiedDate(new Date());
                        properties1.setActive(true);
                        properties.add(properties1);
                        for (int k = 0; k < tierVariation.get(1).getOption_list().size(); k++) {
                            Properties properties2 = new Properties();
                            try {
                                properties2.setKeyname(tierVariation.get(1).getName());
                                properties2.setValue(tierVariation.get(1).getOption_list().get(j).getOption());
                            }catch (IndexOutOfBoundsException index){
                                properties2.setKeyname("");
                                properties2.setValue("");
                            }
                            properties2.setCreatedDate(new Date());
                            properties2.setModifiedDate(new Date());
                            properties2.setActive(true);
                            properties.add(properties2);
                            Optional<ProductVariations> productVariations = repoProductVariations.findByBarcode(String.valueOf(modelItems.get(mo).getModel_id()));
                            DtoProductVariations variations = new DtoProductVariations();
                            if (productVariations.isPresent()) {
                                variations.setId(productVariations.get().getId());
                            } else {
                                variations.setId(0L);
                            }
                            if (!tierVariation.get(0).getOption_list().get(k).getImage().getImage_url().isEmpty()) {
                                variations.setImage(tierVariation.get(0).getOption_list().get(k).getImage().getImage_url());
                            }
                            variations.setBarcode(String.valueOf(modelItems.get(mo).getModel_id()));
                            variations.setSku(modelResponseDTO.getResponse().getModel().get(mo).getModel_sku());
                            variations.setProperties(properties);
//                            variations.setQuantity(String.valueOf(modelItems.get(mo).getStock_info().get(0).getCurrent_stock()));
                            variations.setQuantity("");
                            variations.setWeight(c.getWeight() * 1000);
                            variations.setPrice((double) modelItems.get(mo).getPrice_info().get(0).getOriginal_price());
                            variations.setSaleprice((double) modelItems.get(mo).getPrice_info().get(0).getCurrent_price());
                            variations.setActive(true);
                            dtoProductVariations.add(variations);
                            mo++;
                        }
                    }
                } else {
                    for (int j = 0; j < tierVariation.size(); j++) {
                        for (int k = 0; k < tierVariation.get(j).getOption_list().size(); k++) {
                            Properties properties2 = new Properties();
                            properties2.setKeyname(tierVariation.get(j).getName());
                            properties2.setValue(tierVariation.get(j).getOption_list().get(k).getOption());
                            properties2.setCreatedDate(new Date());
                            properties2.setModifiedDate(new Date());
                            properties2.setActive(true);
                            properties.add(properties2);
                            Optional<ProductVariations> productVariations = repoProductVariations.findByBarcode(String.valueOf(modelItems.get(mo).getModel_id()));
                            DtoProductVariations variations = new DtoProductVariations();
                            if (productVariations.isPresent()) {
                                variations.setId(productVariations.get().getId());
                                variations.setProductId(productVariations.get().getProducts().getId());
                                variations.setProductName(productVariations.get().getProducts().getTitle());
                            } else {
                                variations.setId(0L);
                            }
                            if (!tierVariation.get(j).getOption_list().get(k).getImage().getImage_url().isEmpty()) {
                                variations.setImage(tierVariation.get(j).getOption_list().get(k).getImage().getImage_url());
                            }
                            variations.setBarcode(String.valueOf(modelItems.get(mo).getModel_id()));
                            variations.setSku("sp"+modelResponseDTO.getResponse().getModel().get(mo).getModel_sku());
                            variations.setProperties(properties);
//                            variations.setQuantity(String.valueOf(modelItems.get(mo).getStock_info().get(0).getCurrent_stock()));
                            variations.setQuantity("1");
                            variations.setWeight(c.getWeight() * 1000);
                            variations.setPrice((double) modelItems.get(mo).getPrice_info().get(0).getOriginal_price());
                            variations.setSaleprice((double) modelItems.get(mo).getPrice_info().get(0).getCurrent_price());
                            variations.setActive(true);
                            dtoProductVariations.add(variations);
                            mo++;
                        }
                    }
                }
                Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeId);
                if (c.getPrice_info() != null) {
                    for (PriceInfo p : c.getPrice_info()) {
                        dtoProduct.setPrice((double) p.getOriginal_price());
                        dtoProduct.setSaleprice((double) p.getCurrent_price());
                    }
                } else {
                    dtoProduct.setPrice(0.0);
                    dtoProduct.setSaleprice(0.0);
                }
                int quantity = 0;
                if (c.getStock_info() != null) {
                    for (StockInfo s :
                            c.getStock_info()) {
                        quantity = quantity + s.getCurrent_stock();
                    }
                    dtoProduct.setQuantity(String.valueOf(quantity));
                } else dtoProduct.setQuantity("0");
                List<String> tag = new ArrayList<>();
                dtoProduct.setTags(tag);
                dtoProduct.setChannel("Shopee" + " - " + channelShopee.get().getShopeeShopName());
                dtoProduct.setVariations(dtoProductVariations);
                dtoProduct.setContent(c.getDescription());
                dtoProduct.setShopinfoId(shopId);
                dtoProduct.setShopeeId(shopeeId);
                if (c.getImage() != null) {
                    dtoProduct.setImages(c.getImage().getImage_url_list());
                }
                dtoProduct.setProductIdShopee(c.getItem_id());
                Optional<ProductShopee> productShopee = repoProductShopee.findByItemIdAndShopId(c.getItem_id(), shopId);
                if (productShopee.isPresent()) {
                    dtoProduct.setId(productShopee.get().getProduct().getId());
                    serProduct.updateProduct(dtoProduct, shopId);
                } else {
                    serProduct.InsSent(dtoProduct, currentUser);
                }
            }
        }
        if (itemResponseDTO == null || itemResponseDTO.getResponse().isHasNextPage() == false) {
            return false;
        }
        if (pageNumber > 400) {
            return false;
        }
        return true;
    }

    public boolean getListItem(UserPrincipal currentUser, int pageNumber, Long shopeeId, Long shopId) {
        long timestamp = Common.getCurrentTime();
        GetListItemRequestDTO requestItem = new GetListItemRequestDTO(partnerID, shopeeId, timestamp, 100,
                pageNumber);
        int cnt = 0;
        GetListItemResponseDTO getListItemResponseDTO = null;
        while (getListItemResponseDTO == null && cnt < 5) {
            cnt++;
            try {
                getListItemResponseDTO = Common.callAPI(partnerKey, requestItem, Iconstants.GETLISTITEM_ENDPOINT,
                        GetListItemResponseDTO.class);
            } catch (Exception e) {
                getListItemResponseDTO = null;
            }
        }

        if (getListItemResponseDTO != null) {
            List<ListItem> items = getListItemResponseDTO.getItems();
            for (ListItem b : items) {
                ItemDetailRequestDTO ready = new ItemDetailRequestDTO(partnerID, shopeeId, timestamp, b.getItem_id());
                ItemDetailsResponseDTO itemDetailsResponseDTO = null;
                cnt = 0;
                while (itemDetailsResponseDTO == null && cnt < 5) {
                    cnt++;
                    try {
                        itemDetailsResponseDTO = Common.callAPI(partnerKey, ready,
                                Iconstants.GETLISTITEM_DETAILS_ENDPOINT, ItemDetailsResponseDTO.class);
                    } catch (Exception e) {
                        itemDetailsResponseDTO = null;
                    }
                }
                VariationDTO variationDTO = null;
                int a = 0;
                while (variationDTO == null && a < 5) {
                    a++;
                    try {
                        variationDTO = Common.callAPI(partnerKey, ready,
                                Iconstants.GET_VARIATION, VariationDTO.class);
                    } catch (Exception e) {
                        variationDTO = null;
                    }
                }
                if (itemDetailsResponseDTO != null && itemDetailsResponseDTO.getItem() != null) {
                    Item item = itemDetailsResponseDTO.getItem();
                    List<String> tag = new ArrayList<>();
//                for (int i = 0; i < item.getAttributes().size(); i++) {
//                    String s = item.getAttributes().get(i).getAttribute_name();
//                    tag.add(s);
//                }
                    tag.add("");
                    List<DtoProductVariations> variants = new ArrayList<>();
                    List<Variation> variationList = itemDetailsResponseDTO.getItem().getVariations();
                    if (item.getVariations().size() > 0) {
                        List<Properties> properties = new ArrayList<>();
                        if (variationDTO != null) {
                            List<TierVariation> tierVariationList = variationDTO.getTier_variation();
                            for (int j = 0; j < tierVariationList.size(); j++) {
                                for (int i = 0; i < tierVariationList.get(j).getOptions().size(); i++) {
                                    Properties properties1 = new Properties();
                                    properties1.setKeyname(tierVariationList.get(j).getName());
                                    properties1.setValue(tierVariationList.get(j).getOptions().get(i));
                                    properties1.setCreatedDate(new Date());
                                    properties1.setModifiedDate(new Date());
                                    properties1.setActive(true);
                                    properties.add(properties1);
                                }
                            }
                            for (int j = 0; j < tierVariationList.size(); j++) {
                                for (int i = 0; i < tierVariationList.get(j).getOptions().size(); i++) {
                                    DtoProductVariations variations = new DtoProductVariations();
                                    Optional<ProductVariations> productVariations = repoProductVariations.findByBarcode(String.valueOf(item.getVariations().get(i).getVariation_id()));
                                    if (!tierVariationList.get(j).getImages_url().isEmpty()) {
                                        productVariations.ifPresent(value -> variations.setId(value.getId()));
                                        variations.setImage(tierVariationList.get(j).getImages_url().get(i));
                                        variations.setBarcode(String.valueOf(item.getVariations().get(i).getVariation_id()));
                                        variations.setSku(item.getVariations().get(i).getVariation_sku());
                                        variations.setProperties(properties);
                                        variations.setQuantity(String.valueOf(item.getVariations().get(i).getStock()));
                                        variations.setWeight(item.getWeight() * 1000);
                                        variations.setPrice(item.getVariations().get(i).getPrice());
                                        variations.setSaleprice(item.getVariations().get(i).getOriginal_price());
                                        variations.setActive(statusVariation(item.getVariations().get(i).getStatus()));
                                        variants.add(variations);
                                    }
                                }
                            }
                        }
                    }
                    List<String> image = item.getImages();
                    DtoProduct dtoProduct = new DtoProduct();
                    dtoProduct.setIsActive(true);
                    dtoProduct.setSku(item.getItem_sku());
                    dtoProduct.setTitle(item.getName());
                    dtoProduct.setPrice(item.getPrice());
                    dtoProduct.setSaleprice(item.getOriginal_price());
                    dtoProduct.setTags(tag);
                    dtoProduct.setVariations(variants);
                    dtoProduct.setContent(item.getDescription());
                    dtoProduct.setShopinfoId(shopId);
                    dtoProduct.setImages(image);
                    dtoProduct.setChannel("Shopee");
                    dtoProduct.setQuantity(String.valueOf(item.getStock()));
                    dtoProduct.setProductIdShopee(b.getItem_id());
                    Optional<ProductShopee> productShopee = repoProductShopee.findByItemIdAndShopId(b.getItem_id(), shopId);
                    if (productShopee.isPresent()) {
                        dtoProduct.setId(productShopee.get().getProduct().getId());
                        serProduct.updateProduct(dtoProduct, shopId);
                    } else {
                        serProduct.InsSent(dtoProduct, currentUser);
                    }
//                        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoProduct));
                }
            }
            if (getListItemResponseDTO.getTotal() <= (pageNumber + 1) * 100) {
                return false;
            }
            return true;
        }
        return false;
    }

    public ResponseEntity<?> upProductToShopee(RequestProduct requestProduct, Long shopeeId, String partnerKey, Long partnerID) {
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 1L);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/product/add_item";
        String image = "https://landapi.vipage.vn/vvlanding";
        try {
            Optional<Product> product = repoProduct.findById(requestProduct.getProductId());
            if (product.isPresent()) {
                List<String> img = new ArrayList<>();
                for (String s : product.get().getImages()) {
                    String urls = image + s;
                    img.add(uploadImg(shopeeId, partnerKey, partnerID, urls));
                }
                ImageId imageId = new ImageId();
                imageId.setImage_id_list(img);
                List<LogisticInfo> logisticInfos = new ArrayList<>();
                LogisticInfo logistics = new LogisticInfo();
                logistics.setEnabled(true);
                logistics.set_free(false);
                logistics.setLogistic_id(requestProduct.getLogisticId());
                logisticInfos.add(logistics);
                Brand brand = new Brand();
                brand.setBrand_id(1063328);
                brand.setOriginal_brand_name("AAA JEANS");
                AddProduct addProduct = new AddProduct();
                addProduct.setCategory_id(requestProduct.getCategoryId());
                addProduct.setOriginal_price(product.get().getPrice());
                addProduct.setDescription(product.get().getContent());
                addProduct.setWeight(10);
                addProduct.setItem_name(product.get().getTitle());
//                addProduct.setItem_status("NORMAL");
                addProduct.setNormal_stock(100);
                addProduct.setBrand(brand);
                addProduct.setLogistic_info(logisticInfos);
                addProduct.setImage(imageId);
                addProduct.setItem_sku(product.get().getSku());
                ResponseItem ob = Common.callAPIV2(null, host, path, access_token
                        , String.valueOf(shopeeId), partnerKey
                        , String.valueOf(partnerID), addProduct, ResponseItem.class);
                if (ob == null) return ResponseEntity.badRequest().body(Constant.res("", false, null));
                addModel(product.get().getVariations()
                        , ob.getResponse().getItem_id(), access_token
                        , shopeeId, partnerKey, partnerID);
                return ResponseEntity.ok(Constant.res("", true, ob));
            }
            return ResponseEntity.badRequest().body(Constant.res("không tìm thấy sản phẩm", false, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Constant.res(e.getMessage(), false, null));
        }
    }

    public void addModel(List<ProductVariations> productVariations, Long itemId, String token, Long shopeeId, String partnerKey, Long partnerID) throws JsonProcessingException {
        try {
            String host = "https://partner.shopeemobile.com";
            String path = "/api/v2/product/init_tier_variation";
            RequestModelProduct requestModelProduct = new RequestModelProduct();
            requestModelProduct.setItem_id(itemId);
            List<RequestModelProduct.TierVariation> tierVariationList = new ArrayList<>();
            List<RequestModelProduct.Model> models = new ArrayList<>();
            for (ProductVariations p : productVariations) {
                RequestModelProduct.TierVariation tierVariation = new RequestModelProduct.TierVariation();
                tierVariation.setName(p.getProperties().get(0).getKeyname());
                String imageId = uploadImg(shopeeId, partnerKey, partnerID, p.getImage());
                RequestOptionList.Image image = new RequestOptionList.Image();
                image.setImage_id(imageId);
                List<RequestOptionList> requestOptionLists = new ArrayList<>();
                for (Properties properties : p.getProperties()
                ) {
                    requestOptionLists.add(new RequestOptionList(properties.getValue(), image));
                }
                List<Integer> tier_index = new ArrayList<>();
                tier_index.add(0);
                models.add(new RequestModelProduct.Model(tier_index, p.getQuantity().intValue(), p.getPrice(), p.getSku()));
                tierVariation.setOption_list(requestOptionLists);
                tierVariationList.add(tierVariation);
            }
            requestModelProduct.setTier_variation(tierVariationList);
            requestModelProduct.setModel(models);
            Object ob = Common.callAPIV2(null, host, path, token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), requestModelProduct, Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<?> getLogistic(Long shopeeId, String partnerKey, Long partnerID) {
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 2L);
        System.out.println(access_token);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/logistics/get_channel_list";
        try {
            GetLogisticDTO getLogistic = Common.callAPIV2(null, host, path, access_token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), null, GetLogisticDTO.class);
            return ResponseEntity.ok(Constant.res("", true, getLogistic));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Constant.res(e.getMessage(), false, null));
        }
    }

    public ResponseEntity<?> getBrand(Long shopeeId, int offset, Long categoryId, String partnerKey, Long partnerID) {
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 1L);
        System.out.println(access_token);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/product/get_brand_list";
        String url = "?category_id=" + categoryId + "&offset=" + offset + "&page_size=20&status=1&language=vi";
        try {
            Object ob = Common.callAPIV2(url, host, path, access_token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), null, Object.class);
            return ResponseEntity.ok(Constant.res("", true, ob));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Constant.res(e.getMessage(), false, null));
        }
    }

    public String uploadImg(Long shopeeId, String partnerKey, Long partnerID, String file) {
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 1L);
        String host = "https://partner.shopeemobile.com";
        String path_img = "/api/v2/media_space/upload_image";
        long timestamp = Common.getCurrentTime();
        String base = partnerID + path_img + timestamp + access_token + shopeeId;
        String sign = Common.hash256(partnerKey, base);
        String url = host + path_img + "?access_token=" + access_token + "&shop_id=" + shopeeId + "&partner_id=" + partnerID + "&sign=" + sign + "&timestamp=" + timestamp;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
            map.add("image", storageService.loadAsResource(file));
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<ResponseImgProduct> responseImg = Iconstants.restTemplate.exchange(url, HttpMethod.POST, entity, ResponseImgProduct.class);
            System.out.println(responseImg.getBody().getMessage() + responseImg.getBody().getError());
            return responseImg.getBody().getResponse().getImage_info().getImage_id();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> uploadImgTEST(Long shopeeId, Long productId, String partnerKey, Long partnerID) {
        String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 1L);
        Optional<Product> product = repoProduct.findById(productId);
        String image = "https://landapi.vipage.vn/vvlanding" + product.get().getImages().get(0);
        String host = "https://partner.shopeemobile.com";
        String path_img = "/api/v2/media_space/upload_image";
        long timestamp = Common.getCurrentTime();
        String base = partnerID + path_img + timestamp + access_token + shopeeId;
        String sign = Common.hash256(partnerKey, base);
        String url = host + path_img + "?access_token=" + access_token + "&shop_id=" + shopeeId + "&partner_id=" + partnerID + "&sign=" + sign + "&timestamp=" + timestamp;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
            map.add("image", storageService.loadAsResource(image));
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<Object> responseImg = Iconstants.restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
            return ResponseEntity.ok(responseImg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> getCategory(Long shopeeId, String partnerKey, Long partnerID) {
        String host = "https://partner.shopeemobile.com";
        String path_img = "/api/v2/product/get_category";
        try {
            String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 1L);
            String url = "?language=vi";
            CategoryListDTO object = Common.callAPIV2(url, host, path_img, access_token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), null, CategoryListDTO.class);
            if (object.getResponse() != null) {
                for (CategoryList c : object.getResponse().getCategory_list()) {
                    CategoryShopee categoryShopee = new CategoryShopee();
                    categoryShopee.setCategoryId(c.getCategory_id());
                    categoryShopee.setParentCategoryId(c.getParent_category_id());
                    categoryShopee.setOriginalCategoryName(c.getOriginal_category_name());
                    categoryShopee.setDisplayCategoryName(c.getDisplay_category_name());
                    categoryShopee.setHasChildren(c.isHas_children());
                    repoCategoryShopee.save(categoryShopee);
                }
                return ResponseEntity.ok(Constant.res("", true, null));
            } else {
                return ResponseEntity.badRequest().body(Constant.res(object.getError(), false, null));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Constant.res(e.getMessage(), false, null));
        }
    }

    public ResponseEntity<?> getAttributes(Long category_id, Long shopeeId, String partnerKey, Long partnerID) {
        String host = "https://partner.shopeemobile.com";
        String path_img = "/api/v2/product/get_attributes";
        String url = "?category_id=" + category_id + "&language=vi";
        try {
            String access_token = shopeeService.getToken(shopeeId, partnerKey, partnerID, 1L);
            Object object = Common.callAPIV2(url, host, path_img, access_token, String.valueOf(shopeeId), partnerKey, String.valueOf(partnerID), null, Object.class);
            return ResponseEntity.ok(Constant.res("", true, object));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Constant.res(e.getMessage(), false, null));
        }
    }

    public ResponseEntity<?> getCategoryByName(String name) {
        try {
            return ResponseEntity.ok(Constant.res("", true, repoCategoryShopee.findByDisplayCategoryNameContaining(name)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Constant.res(e.getMessage(), false, null));
        }
    }

    private Boolean statusVariation(String str) {
        if (str.contains("MODEL_NORMAL")) return true;
        else return false;
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[8];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "qwertyuiopasdfghjklzxcvbnm0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public ResponseEntity<?> test(String file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            Resource resource = storageService.loadAsResource(file);
//        Resource resource1 = storageService.loadAsResource("C:\\Users\\sang\\Desktop\\IT\\vv\\vvlandingpagee\\product_images\\product_images\\1\\491cfd64-5589-4975-9e90-74ff1867a1b0.jpg");
            map.put("1", resource.exists());
            map.put("2", resource.isReadable());
            map.put("3", resource.contentLength());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(map);
        }
    }

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://thuthuatnhanh.com/wp-content/uploads/2019/11/hinh-nen-dien-thoai-dep.jpg");
        BufferedImage img = ImageIO.read(url);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", bos);
        System.out.println(Arrays.toString(bos.toByteArray()));
    }
}
