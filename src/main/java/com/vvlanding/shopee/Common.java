package com.vvlanding.shopee;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Common {

    private static RestTemplate restTemplate = new RestTemplate();

    private static byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return hmacSha256;
    }

    public static String hash256(String key, String msg) {
        try {
            byte[] hmacSha256 = calcHmacSha256(key.getBytes("UTF-8"), msg.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, hmacSha256));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }

    public static long getCurrentTime() {
        String timestampString = String.valueOf(System.currentTimeMillis());
        timestampString = timestampString.substring(0, timestampString.length() - 3);
        return Long.parseLong(timestampString);
    }

    public static long date2Timestamp(Date date) {
        String timestampString = String.valueOf(date.getTime());
        timestampString = timestampString.substring(0, timestampString.length() - 3);
        return Long.parseLong(timestampString);
    }

    public static <T> T callAPI(String partnerKey, Object bodyObj, final String endPoint, Class<T> myClass) {
        try {
            String body = Iconstants.MAPPER.writeValueAsString(bodyObj);
            String msg = endPoint + "|" + body;
            String Authorization = Common.hash256(partnerKey, msg);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", Authorization);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(body, headers);

            ResponseEntity<T> response = Iconstants.restTemplate.postForEntity(new URI(endPoint), entity, myClass);
            return response.getBody();

        } catch (JsonProcessingException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T callAPIV2(String urls,String host,String path,String accessToken,String shopId,String partnerKey ,String partner_id, Object bodyObj, Class<T> myClass) {
        Map<String, Object> res = new HashMap<>();
        try {
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            ObjectMapper mapper = new ObjectMapper();
            long time = Common.getCurrentTime();
            String sing = partner_id+path+time+accessToken+shopId;
            String Authorization = Common.hash256(partnerKey, sing);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (urls != null){
                String url = host+path+urls+"&partner_id="+partner_id+"&timestamp="+time+"&shop_id="+shopId+"&access_token="+accessToken+"&sign="+Authorization;
                if (bodyObj != null){
                    if (bodyObj.equals("no")){
                        HttpEntity<String> entity = new HttpEntity<>(headers);
                        ResponseEntity<T> response = restTemplate.postForEntity(new URI(url),entity,myClass);
                        return response.getBody();
                    }else {
                        String body = Iconstants.MAPPER.writeValueAsString(bodyObj);
                        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
                        ResponseEntity<T> response = restTemplate.postForEntity(new URI(url), entity, myClass);
                        return response.getBody();
                    }
                }else {
                    HttpEntity<String> entity = new HttpEntity<String>(headers);
                    ResponseEntity<T> response = restTemplate.exchange(new URI(url) , HttpMethod.GET, entity,myClass);
                    return response.getBody();
                }
            }else {
                String url = host+path+"?partner_id="+partner_id+"&timestamp="+time+"&shop_id="+shopId+"&access_token="+accessToken+"&sign="+Authorization;
                if (bodyObj != null){
                    mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
                    String body = mapper.writeValueAsString(bodyObj);
                    HttpEntity<String> entity = new HttpEntity<String>(body, headers);
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyObj));
                    ResponseEntity<T> response = restTemplate.postForEntity(new URI(url), entity, myClass);
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
                    return response.getBody();
                }else {
                    HttpEntity<String> entity = new HttpEntity<String>(headers);
                    ResponseEntity<T> response = restTemplate.exchange(new URI(url) , HttpMethod.GET, entity,myClass);
                    return response.getBody();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String createToken2Auth(String redirectURL, String partnerKey) {
        String baseStr = partnerKey + redirectURL;
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(baseStr);
    }
}
