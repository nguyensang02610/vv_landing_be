package com.vvlanding.shopee.service.shopee;


import com.vvlanding.shopee.Common;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.shopee.auth.ShopAuthReqDTO;
import com.vvlanding.shopee.auth.ShopAuthResDTO;


public class ShopAuthServices {

    public static ShopAuthResDTO authShop(ShopAuthReqDTO shopAuthReqDTO) {
        String secretToken = Common.createToken2Auth(shopAuthReqDTO.getRedirectUrl(), shopAuthReqDTO.getPartnerKey());
        String authLink = Iconstants.SHOP_AUTH + "?id=" + shopAuthReqDTO.getPartnerId() + "&token=" +  secretToken + "&redirect=" + shopAuthReqDTO.getRedirectUrl() ;
//        String authLink = "https://partner.test-stable.shopeemobile.com/api/v1/shop/auth_partner" + "?id=" + shopAuthReqDTO.getPartnerId() + "&token=" +  secretToken + "&redirect=" + shopAuthReqDTO.getRedirectUrl() ;
        return new ShopAuthResDTO(authLink, Common.getCurrentTime());
    }

    public static String authShopV2(long partner_id,String red,String key){
        long time = Common.getCurrentTime();
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/shop/auth_partner";
        String base = partner_id + path + time;
        String sign = Common.hash256(key,base);
        String url = host+path+"?partner_id="+partner_id+"&redirect="+red+"&timestamp="+time+"&sign="+sign;
        return url;
    }
    public static String authShopV2Test(long partner_id,String red,String key){
        long time = Common.getCurrentTime();
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/shop/auth_partner";
        String base = partner_id + path + time;
        String sign = Common.hash256(key,base);
        String url = host+path+"?partner_id="+partner_id+"&redirect="+red+"&timestamp="+time+"&sign="+sign;
        return url;
    }

    public static String cancelAppV2(String red,String key,Long partnerId){
        long time = Common.getCurrentTime();
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/shop/cancel_auth_partner";
        String base = partnerId + path + time;
        String sign = Common.hash256(key,base);
        String url = host+path+"?partner_id="+partnerId+"&timestamp="+time+"&sign="+sign+"&redirect="+red;
        return url;
    }

}
