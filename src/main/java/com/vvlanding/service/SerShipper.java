package com.vvlanding.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.dto.shipped.ghtk.DtoShipped;
import com.vvlanding.dto.shipped.viettel.LoginResponseDTO;
import com.vvlanding.dto.shipped.viettel.LoginViettel;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.repo.RepoShipped;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.table.Shipped;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class SerShipper {

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoShipped repoShipped;

    @Autowired
    SerGHN serGHN;

    private final String URL = "https://partner.viettelpost.vn/v2/user/Login";

    public ResponseEntity insertShipper(LoginViettel login, Long shopId, String token, String name, Long type) throws JsonProcessingException, URISyntaxException {
        Map<String,Object> response = new HashMap<>();
        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);
        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name,shopId);
        Optional<Shipped> shippedOptional = repoShipped.findByToken(token);
        ObjectMapper mapper = new ObjectMapper();
        if (login.getUSERNAME() != null && login.getPASSWORD() != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            try {
                mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
                HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(login), headers);
                ResponseEntity<LoginResponseDTO> responseEntity = Iconstants.restTemplate.exchange(new URI(URL),HttpMethod.POST,entity,LoginResponseDTO.class);
                try {
                    if (responseEntity.getBody().getData().getToken() != null){
                        Optional<Shipped> s = repoShipped.findByToken(responseEntity.getBody().getData().getToken());
                        if (s.isPresent()){
                            response.put("success","false");
                            response.put("message","Đã có đơn vị vận chuyển");
                            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
                        }
                        if (shipped.isPresent()){
                            Shipped shipped1 = shipped.get();
                            shipped1.setToken(responseEntity.getBody().getData().getToken());
                            shipped1.setShopInfo(shopInfo.get());
                            repoShipped.save(shipped1);
                        }else {
                            Shipped shipped1 = new Shipped();
                            shipped1.setName(name);
                            shipped1.setToken(responseEntity.getBody().getData().getToken());
                            shipped1.setShopInfo(shopInfo.get());
                            repoShipped.save(shipped1);
                        }

                        response.put("success","true");
                        response.put("message","thêm thành công");
                        return new ResponseEntity(response, HttpStatus.OK);
                    }
                }catch (NullPointerException e){
                    response.put("success","false");
                    response.put("message","Tài khoản hoặc mật khẩu sai");
                    return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
                }
            }catch (JsonProcessingException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (type == 0){
            if (!shopInfo.isPresent()){
                response.put("success","false");
                response.put("message","không tim thấy thông tin shop");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
            if (shipped.isPresent() || shippedOptional.isPresent()){
                response.put("success","false");
                response.put("message","Đã có đơn vị vận chuyển");
                return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
            }
            Shipped shipped2 = new Shipped();
            shipped2.setName(name);
            shipped2.setToken(token);
            shipped2.setShopInfo(shopInfo.get());
            repoShipped.save(shipped2);
            Optional<Shipped> shipped1 = repoShipped.findByToken(token);
            if (shipped1.isPresent() && shipped1.get().getName().contains("Giao Hàng Nhanh")){
                serGHN.getShop(shipped1.get(),token,type);
            }
            response.put("data",shipped2);
            return new ResponseEntity(response,HttpStatus.OK);
        }else if (type == 1){
            if (shippedOptional.isPresent()){
                response.put("success","false");
                response.put("message","token đã tồn tại");
                return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
            }
            Shipped shipped1 = shipped.get();
            shipped1.setToken(token);
            repoShipped.save(shipped1);
            Optional<Shipped> shipped2 = repoShipped.findByToken(token);
            if (shipped2.isPresent() && shipped2.get().getName().contains("Giao Hàng Nhanh")){
                serGHN.getShop(shipped2.get(),token,type);
            }
            response.put("message","update thành công");
            return new ResponseEntity(response,HttpStatus.OK);
        }
        return null;
    }
    public ResponseEntity GetShipper(String name, ShopInfo shopInfo) {
        Map<String, Object> response = new HashMap<>();
        Optional<Shipped> opShipped = repoShipped.findByNameAndShopInfoId(name, shopInfo.getId());
        Shipped shipped1 = opShipped.get();
        if (!opShipped.isPresent()) {
            response.put("message", "Không tìm thấy thông tin !!! ");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            DtoShipped dtoShipped = new DtoShipped();
            dtoShipped.setToken(shipped1.getToken());
            dtoShipped.setName(shipped1.getName());
            dtoShipped.setId(shipped1.getId());
            response.put("data", dtoShipped);
            response.put("message", "Thông tin vận chuyển");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }
    public ResponseEntity getAllShipper(Long shopId){
        List<Shipped> shipped = repoShipped.findAllByShopInfoId(shopId);
        List<DtoShipped> dtoShippeds = new ArrayList<>();
        for (Shipped s: shipped) {
            DtoShipped dtoShipped = new DtoShipped();
            dtoShipped.setToken(s.getToken());
            dtoShipped.setName(s.getName());
            dtoShipped.setId(s.getId());
            dtoShippeds.add(dtoShipped);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data", dtoShippeds);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
