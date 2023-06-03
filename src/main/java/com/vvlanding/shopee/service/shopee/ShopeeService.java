package com.vvlanding.shopee.service.shopee;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.dto.DtoChannelShopee;
import com.vvlanding.shopee.*;
import com.vvlanding.shopee.auth.RequestACCESS_TOKEN;
import com.vvlanding.shopee.auth.ResponseACCESS_TOKEN;
import com.vvlanding.shopee.auth.ResponseRefresh_Token;
import com.vvlanding.repo.RepoChannelShopee;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.repo.RepoShopeeToken;
import com.vvlanding.table.ChannelShopee;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.table.ShopeeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShopeeService {

    private final long partnerIDV2 = Iconstants.partner_id_customer_v2;

    private final String partnerKeyV2 = Iconstants.test_key_customer_v2;

    @Autowired
    private RepoChannelShopee repoChannelShopee;

    @Autowired
    private RepoShopInfo repoShopInfo;

    @Autowired
    private RepoShopeeToken repoShopeeToken;

    public List<DtoChannelShopee> getShopeeOfShopByQuery(ShopInfo shopInfo, String s, String query) {
        List<DtoChannelShopee> listDtoChannelShopee = new ArrayList<>();
        List<ChannelShopee> listChannelShopeeQuery = repoChannelShopee.findAllByShopeeShopName(query);
        if (listChannelShopeeQuery.size() > 0) {
            return getDtoChannelShopee(listDtoChannelShopee, listChannelShopeeQuery);
        }
        return listDtoChannelShopee;
    }

    public List<DtoChannelShopee> getShopeeOfShopByQueryPage(ShopInfo shopInfo, String s, String query, Pageable pageable) {
        List<DtoChannelShopee> listDtoChannelShopee = new ArrayList<>();
        List<ChannelShopee> listChannelShopeeQuery = repoChannelShopee.findAllByShopeeShopName(query);
        if (listChannelShopeeQuery.size() > 0) {
            return getDtoChannelShopee(listDtoChannelShopee, listChannelShopeeQuery);
        }
        return listDtoChannelShopee;
    }

    public List<DtoChannelShopee> getShopeeOfShop(ShopInfo shopInfo) {
        List<DtoChannelShopee> listDtoChannelShopee = new ArrayList<>();
        List<ChannelShopee> listChannelShopee = repoChannelShopee.findAllByShopId(shopInfo.getId());
        return getDtoChannelShopee(listDtoChannelShopee, listChannelShopee);
    }

    public List<DtoChannelShopee> getShopeeOfShopPage(ShopInfo shopInfo, Pageable pageable) {
        List<DtoChannelShopee> listDtoChannelShopee = new ArrayList<>();
        List<ChannelShopee> listChannelShopee = repoChannelShopee.findAllByShopId(shopInfo.getId(), pageable);
        return getDtoChannelShopee(listDtoChannelShopee, listChannelShopee);
    }

    private List<DtoChannelShopee> getDtoChannelShopee(List<DtoChannelShopee> listDtoChannelShopee, List<ChannelShopee> listChannelShopee) {
        for (ChannelShopee b : listChannelShopee) {
            DtoChannelShopee dtoChannelShopee = new DtoChannelShopee();
            dtoChannelShopee.ChannelShopee(b.getId(), b.getShop().getId(), b.getShopeeShopId(),
                    b.getShopeeShopName(), b.getShopeeShopDesc(), b.getShopeeShopImage());
            listDtoChannelShopee.add(dtoChannelShopee);
        }
        return listDtoChannelShopee.stream()
                .sorted(Comparator.comparing(DtoChannelShopee::getId).reversed())
                .collect(Collectors.toList());
    }
    public ResponseEntity<?> getShop(ShopInfo shopInfo, Long shopeeShopId,String partnerKey , Long partnerID,Long appId){
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/shop/get_shop_info";
        String access_token = getToken(shopeeShopId,partnerKey,partnerID,appId);
        ChannelShopee shopee = new ChannelShopee();
        Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeShopId);
        Map<String,Object> res = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            int cnt = 0;
            ShopInfoResponse shopInfoResponse = null;
            while (shopInfoResponse == null && cnt < 5){
                cnt++;
                shopInfoResponse = Common.callAPIV2(null,host,path,access_token,String.valueOf(shopeeShopId),partnerKey,String.valueOf(partnerID),null,ShopInfoResponse.class);
            }
            if (channelShopee.isPresent()){
                if (!Objects.equals(channelShopee.get().getShop().getId(), shopInfo.getId())){
                    res.put("message","tài khoản shopee đã đăng ký ở shop landing khác");
                    res.put("success",false);
                    return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
                }
                shopee = channelShopee.get();
                shopee.setShopeeShopName(shopInfoResponse.getShop_name());
                shopee.setShopeeShopId(shopeeShopId);
                shopee.setRegion(shopInfoResponse.getRegion());
                shopee.setShipAffiShops(objectMapper.writeValueAsString(shopInfoResponse.getSip_affi_shops()));
                shopee.setStatus(shopInfoResponse.getStatus());
                repoChannelShopee.save(shopee);
                res.put("data",shopee);
                res.put("success",true);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }else {
                shopee.setShop(shopInfo);
                shopee.setShopeeShopName(shopInfoResponse.getShop_name());
                shopee.setShopeeShopId(shopeeShopId);
                shopee.setRegion(shopInfoResponse.getRegion());
                shopee.setShipAffiShops(objectMapper.writeValueAsString(shopInfoResponse.getSip_affi_shops()));
                shopee.setStatus(shopInfoResponse.getStatus());
                repoChannelShopee.save(shopee);
                res.put("data",shopee);
                res.put("success",true);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public ResponseEntity<?> getShopInfoV2(ShopInfo shopInfo, Long shopeeShopId,String partnerKey , Long partnerID,Long appId,String access_token){
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/shop/get_shop_info";
        ChannelShopee shopee = new ChannelShopee();
        Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeShopId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            int cnt = 0;
            ShopInfoResponse shopInfoResponse = null;
            while (shopInfoResponse == null && cnt < 5){
                cnt++;
                shopInfoResponse = Common.callAPIV2(null,host,path,access_token,String.valueOf(shopeeShopId),partnerKey,String.valueOf(partnerID),null,ShopInfoResponse.class);
            }
            String host2 = "https://partner.shopeemobile.com";
            String path2 = "/api/v2/shop/get_profile";
            int cnt2 = 0;
            ShopInfoGetProfileDTO shopInfoGetProfileDTO = null;
            while (shopInfoGetProfileDTO == null && cnt2 <5){
                cnt2++;
                shopInfoGetProfileDTO = Common.callAPIV2(null,host2,path2,access_token,String.valueOf(shopeeShopId),partnerKey,String.valueOf(partnerID),null,ShopInfoGetProfileDTO.class);
            }
            Map<String,Object> res = new HashMap<>();
            if (channelShopee.isPresent()){
                if (channelShopee.get().getShop().getId() != shopInfo.getId()){
                    res.put("message","tài khoản shopee đã đăng ký ở shop landing khác");
                    res.put("success",false);
                    return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
                }
                shopee = channelShopee.get();
                shopee.setShopeeShopName(shopInfoResponse.getShop_name());
                shopee.setShopeeShopId(shopeeShopId);
                shopee.setRegion(shopInfoResponse.getRegion());
                shopee.setShipAffiShops(objectMapper.writeValueAsString(shopInfoResponse.getSip_affi_shops()));
                shopee.setStatus(shopInfoResponse.getStatus());
                shopee.setShopeeShopImage(shopInfoGetProfileDTO.getResponse().getShop_logo());
                shopee.setShopeeShopDesc(shopInfoGetProfileDTO.getResponse().getDescription());
                repoChannelShopee.save(shopee);
                res.put("data",shopee);
                res.put("success",true);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }else {
                shopee.setShop(shopInfo);
                shopee.setShopeeShopName(shopInfoResponse.getShop_name());
                shopee.setShopeeShopId(shopeeShopId);
                shopee.setRegion(shopInfoResponse.getRegion());
                shopee.setShipAffiShops(objectMapper.writeValueAsString(shopInfoResponse.getSip_affi_shops()));
                shopee.setStatus(shopInfoResponse.getStatus());
                shopee.setShopeeShopImage(shopInfoGetProfileDTO.getResponse().getShop_logo());
                shopee.setShopeeShopDesc(shopInfoGetProfileDTO.getResponse().getDescription());
                repoChannelShopee.save(shopee);
                res.put("data",shopee);
                res.put("success",true);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    public ShopInforResponseDTO getShopInfo(String partnerKey, ShopInforRequestDTO inforRequestDTO,Long shopeeShopId,Long shopId) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        String body = mapper.writeValueAsString(inforRequestDTO);
//        String msg = Iconstants.SHOPEE_ENDPOINT_GET_INFOR + "|" + body;
//
//        String Authorization = Common.hash256(partnerKey, msg);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", Authorization);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        try {
//            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
//            Optional<ShopInfo> shop = repoShopInfo.findById(shopId);
//            HttpEntity<String> entity = new HttpEntity<String>(body, headers);
//
//            ResponseEntity<ShopInforResponseDTO > shopInfo = Iconstants.restTemplate.postForEntity(new URI(Iconstants.SHOPEE_ENDPOINT_GET_INFOR), entity,
//                    ShopInforResponseDTO.class);
//
//            if (shopInfo != null) {
//                long id = 0;
//                String shopeeShopName = shopInfo.getBody().getShop_name();
//                String shopeeShopDesc = shopInfo.getBody().getShop_description();
//                String shopeeShopImage = null;
//                String[] images = shopInfo.getBody().getImages();
//                if (images != null && images.length > 0) {
//                    shopeeShopImage = images[0];
//                }
//                Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeShopId);
//                if (channelShopee.isPresent()) {
//                    ChannelShopee channelShopee1 = channelShopee.get();
//                    channelShopee1.setShopeeShopName(shopeeShopName);
//                    channelShopee1.setShopeeShopDesc(shopeeShopDesc);
//                    channelShopee1.setShopeeShopImage(shopeeShopImage);
//                    repoChannelShopee.save(channelShopee1);
//                } else {
//                    ChannelShopee channelShop = new ChannelShopee(id, shop.get(), shopeeShopId, shopeeShopName, shopeeShopDesc,
//                            shopeeShopImage,"","","");
//                    repoChannelShopee.save(channelShop);
//                }
//
//            }
//            return shopInfo.getBody();
//
//        } catch ( URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public ResponseEntity<?> getAccess_token(ShopInfo shopInfo, String code, Long shop_id,String partnerKey , Long partnerID,Long appId)  {
        ObjectMapper mapper = new ObjectMapper();
        RequestACCESS_TOKEN access_token = new RequestACCESS_TOKEN(code,shop_id,partnerID);
        long timestamp = Common.getCurrentTime();
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/auth/token/get";
        String base = partnerID+path+timestamp;
        String sign = Common.hash256(partnerKey,base);
        String url = host+path+"?partner_id="+partnerID+"&timestamp="+timestamp+"&sign="+sign;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            String body = mapper.writeValueAsString(access_token);
            HttpEntity<String> entity = new HttpEntity<String>(body,headers);
            ResponseEntity<ResponseACCESS_TOKEN > res = Iconstants.restTemplate.postForEntity(new URI(url),entity,ResponseACCESS_TOKEN.class);
            Optional<ShopeeToken> token = repoShopeeToken.findByShopeeIdAndAppId(shop_id,appId);
            if (token.isPresent()){
                if (Objects.requireNonNull(res.getBody()).getAccess_token()!= null || res.getBody().getRefresh_token() !=null){
                    ShopeeToken shopeeToken = token.get();
                    shopeeToken.setShopeeId(shop_id);
                    shopeeToken.setAccess_token(res.getBody().getAccess_token());
                    shopeeToken.setRefresh_token(res.getBody().getRefresh_token());
                    shopeeToken.setShopInfo(shopInfo);
                    shopeeToken.setCreateDate(new Date());
                    repoShopeeToken.save(shopeeToken);
                }
            }
            else {
                ShopeeToken shopeeToken = new ShopeeToken();
                shopeeToken.setShopeeId(shop_id);
                shopeeToken.setAccess_token(res.getBody().getAccess_token());
                shopeeToken.setRefresh_token(res.getBody().getRefresh_token());
                shopeeToken.setShopInfo(shopInfo);
                shopeeToken.setAppId(appId);
                shopeeToken.setCreateDate(new Date());
                repoShopeeToken.save(shopeeToken);
            }
            return res;
        }catch (URISyntaxException | JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
    public ResponseEntity<?> getAccess_token_Test(ShopInfo shopInfo, String code, Long shop_id,String partnerKey , Long partnerID,Long appId)  {
        ObjectMapper mapper = new ObjectMapper();
        RequestACCESS_TOKEN access_token = new RequestACCESS_TOKEN(code,shop_id,partnerID);
        long timestamp = Common.getCurrentTime();
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/auth/token/get";
        String base = partnerID+path+timestamp;
        String sign = Common.hash256(partnerKey,base);
        String url = host+path+"?partner_id="+partnerID+"&timestamp="+timestamp+"&sign="+sign;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            String body = mapper.writeValueAsString(access_token);
            HttpEntity<String> entity = new HttpEntity<String>(body,headers);
            ResponseEntity<ResponseACCESS_TOKEN > res = Iconstants.restTemplate.postForEntity(new URI(url),entity,ResponseACCESS_TOKEN.class);
            Optional<ShopeeToken> token = repoShopeeToken.findByShopeeIdAndAppId(shop_id,appId);
            if (token.isPresent()){
                if (Objects.requireNonNull(res.getBody()).getAccess_token()!= null || res.getBody().getRefresh_token() !=null){
                    ShopeeToken shopeeToken = token.get();
                    shopeeToken.setShopeeId(shop_id);
                    shopeeToken.setAccess_token(res.getBody().getAccess_token());
                    shopeeToken.setRefresh_token(res.getBody().getRefresh_token());
                    shopeeToken.setShopInfo(shopInfo);
                    shopeeToken.setCreateDate(new Date());
                    repoShopeeToken.save(shopeeToken);
                }
            }
            else {
                ShopeeToken shopeeToken = new ShopeeToken();
                shopeeToken.setShopeeId(shop_id);
                shopeeToken.setAccess_token(res.getBody().getAccess_token());
                shopeeToken.setRefresh_token(res.getBody().getRefresh_token());
                shopeeToken.setShopInfo(shopInfo);
                shopeeToken.setAppId(appId);
                shopeeToken.setCreateDate(new Date());
                repoShopeeToken.save(shopeeToken);
            }
            return res;
        }catch (URISyntaxException | JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<?> getAccess_token_merchant(ShopInfo shopInfo, String code, Long merchantId)  {
        ObjectMapper mapper = new ObjectMapper();
        RequestACCESS_TOKEN access_token = new RequestACCESS_TOKEN();
        access_token.setCode(code);
        access_token.setMain_account_id(merchantId.intValue());
        access_token.setPartner_id(partnerIDV2);
        long timestamp = Common.getCurrentTime();
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/auth/token/get";
        String base = partnerIDV2+path+timestamp;
        String sign = Common.hash256(partnerKeyV2,base);
        String url = host+path+"?partner_id="+partnerIDV2+"&timestamp="+timestamp+"&sign="+sign;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            String body = mapper.writeValueAsString(access_token);
            HttpEntity<String> entity = new HttpEntity<String>(body,headers);

            ResponseEntity<ResponseACCESS_TOKEN > res = Iconstants.restTemplate.postForEntity(new URI(url),entity,ResponseACCESS_TOKEN.class);

            if (Objects.requireNonNull(res.getBody()).getAccess_token()!= null || res.getBody().getRefresh_token() !=null){
                ShopeeToken shopeeToken = new ShopeeToken();

                shopeeToken.setMerchantId(merchantId);
                shopeeToken.setAccess_token(res.getBody().getAccess_token());
                shopeeToken.setRefresh_token(res.getBody().getRefresh_token());
                shopeeToken.setShopInfo(shopInfo);
                shopeeToken.setCreateDate(new Date());
                repoShopeeToken.save(shopeeToken);
            }
            return res;

        }catch (URISyntaxException | JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
    public String refreshToken(Long shop_id,String partnerKey , Long partnerID,Long appId)  {
        long timestamp = Common.getCurrentTime();
        ObjectMapper mapper = new ObjectMapper();
        Optional<ShopeeToken> token = repoShopeeToken.findByShopeeIdAndAppId(shop_id,appId);
        RequestACCESS_TOKEN access_token = new RequestACCESS_TOKEN(shop_id,token.get().getRefresh_token(),partnerID);
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/auth/access_token/get";
        String base = partnerID+path+timestamp;
        String sign = Common.hash256(partnerKey,base);
        String url = host+path+"?partner_id="+partnerID+"&timestamp="+timestamp+"&sign="+sign;
        HttpHeaders headers = new HttpHeaders();
        try {
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            String body = mapper.writeValueAsString(access_token);
            HttpEntity<String> entity = new HttpEntity<String>(body,headers);
            ResponseEntity<ResponseRefresh_Token> res = Iconstants.restTemplate.postForEntity(new URI(url),entity,ResponseRefresh_Token.class);
            System.out.println(res.getStatusCodeValue());
            System.out.println(mapper.writeValueAsString(res.getBody()));
            if (Objects.requireNonNull(res.getBody()).getAccess_token()!= null || res.getBody().getRefresh_token() !=null){
                ShopeeToken shopeeToken = token.get();
                shopeeToken.setShopeeId(shop_id);
                shopeeToken.setAccess_token(res.getBody().getAccess_token());
                shopeeToken.setRefresh_token(res.getBody().getRefresh_token());
                shopeeToken.setCreateDate(new Date());
                repoShopeeToken.save(shopeeToken);
            }
            return res.getBody().getAccess_token();
        }catch (URISyntaxException | JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
    public String refreshTokenMerchant(Long merchantId)  {
        long timestamp = Common.getCurrentTime();
        ObjectMapper mapper = new ObjectMapper();
        Optional<ShopeeToken> token = repoShopeeToken.findByMerchantId(merchantId);
        RequestACCESS_TOKEN access_token = new RequestACCESS_TOKEN();
        access_token.setMain_account_id(merchantId.intValue());
        access_token.setPartner_id(partnerIDV2);
        access_token.setRefresh_token(token.get().getRefresh_token());
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/auth/access_token/get";
        String base = partnerIDV2+path+timestamp;
        String sign = Common.hash256(partnerKeyV2,base);
        String url = host+path+"?partner_id="+partnerIDV2+"&timestamp="+timestamp+"&sign="+sign;
        HttpHeaders headers = new HttpHeaders();
        try {
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            String body = mapper.writeValueAsString(access_token);
            HttpEntity<String> entity = new HttpEntity<String>(body,headers);
            ResponseEntity<ResponseRefresh_Token> res = Iconstants.restTemplate.postForEntity(new URI(url),entity,ResponseRefresh_Token.class);
            if (Objects.requireNonNull(res.getBody()).getAccess_token()!= null || res.getBody().getRefresh_token() !=null){
                ShopeeToken shopeeToken = token.get();
                shopeeToken.setMerchantId(merchantId);
                shopeeToken.setAccess_token(res.getBody().getAccess_token());
                shopeeToken.setRefresh_token(res.getBody().getRefresh_token());
                shopeeToken.setCreateDate(new Date());
                repoShopeeToken.save(shopeeToken);
            }
            return res.getBody().getAccess_token();

        }catch (URISyntaxException | JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
    public String getToken(Long value,String partnerKey,Long partnerID,Long appId){
        Optional<ShopeeToken> token = repoShopeeToken.findByShopeeIdAndAppId(value,appId);
        Optional<ShopeeToken> shopeeToken = repoShopeeToken.findByMerchantId(value);
        if (token.isPresent()){
            Date time = new Date();
            long d = time.getTime()/1000 - (token.get().getCreateDate().getTime())/1000;
            if (time.getTime() - token.get().getCreateDate().getTime() > 14390000 && d < 2592000){
                String access_token = refreshToken(value,partnerKey,partnerID,appId);
                return access_token;
            }else if (d > 2592000) {
                return "invalid";
            }else {
                return token.get().getAccess_token();
            }
        }else if (shopeeToken.isPresent()){
            Date time = new Date();
            if (time.getTime() - shopeeToken.get().getCreateDate().getTime() > 14390000){
                String access_token = refreshTokenMerchant(value);
                return access_token;
            }else {
                return shopeeToken.get().getAccess_token();
            }
        }
        return null;
    }
    public List<ChannelShopee> getChannelShopee(Long shopId){
        List<ChannelShopee> channel = repoChannelShopee.findAllByShopId(shopId);
        List<ChannelShopee> channelShopees = new ArrayList<>();
        if (channel.size() >0){
            for (ChannelShopee c: channel) {
                ChannelShopee channelShopee = new ChannelShopee();
                channelShopee.setStatus(c.getStatus());
                channelShopee.setId(c.getId());
                channelShopee.setRegion(c.getRegion());
                channelShopee.setShopeeShopId(c.getShopeeShopId());
                channelShopee.setShopeeShopName(c.getShopeeShopName());
                channelShopee.setShopeeShopDesc(c.getShopeeShopDesc());
                channelShopee.setShopeeShopImage(c.getShopeeShopImage());
                channelShopees.add(channelShopee);
            }
        }
        return channelShopees;
    }
    public ChannelShopee getShopeeById(Long id){
        try {
            return repoChannelShopee.getOne(id);
        }catch (Exception e){
            return null;
        }
    }
}
