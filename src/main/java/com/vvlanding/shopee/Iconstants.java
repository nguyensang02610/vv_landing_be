package com.vvlanding.shopee;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public class Iconstants {

    public static final String SHOP_AUTH = "https://partner.shopeemobile.com/api/v1/shop/auth_partner";
    public static String SHOPEE_ENDPOINT_GET_INFOR = "https://partner.shopeemobile.com/api/v1/shop/get";

    public static final String GET_VARIATION = "https://partner.shopeemobile.com/api/v1/item/tier_var/get";
    public static final String GET_LIST_ORDER = "https://partner.shopeemobile.com/api/v1/orders/basics";
    public static final String GET_DETAIL_ORDER = "https://partner.shopeemobile.com/api/v1/orders/detail";
    public static final String GET_ACCESS_TOKEN = "https://partner.test-stable.shopeemobile.com/api/v2/auth/token/get";

    public static final String GET_ONE_CONVERSATION = "https://partner.shopeemobile.com/api/v2/sellerchat/get_one_conversation";
    public static RestTemplate restTemplate = new RestTemplate();
    public static ObjectMapper MAPPER = new ObjectMapper();

    public static final String GETLISTITEM_ENDPOINT = "https://partner.shopeemobile.com/api/v1/items/get";
    public static final String GETLISTITEM_DETAILS_ENDPOINT = "https://partner.shopeemobile.com/api/v1/item/get";


    public static final long partner_id_order_v2 = 2001409;

    public static final String partner_key_order_v2 = "fdca30d57de2b922ddc566453a03733040ece77da2a92068d1e2da8761ee59a3";

    public static final long partner_id_product_v2 = 2001408;

    public static final String test_key_product_v2 = "9dec10dff4913ecd6e1128ceb688b6a92b635b8c3d11f897802329d69aa3c290";

    public static final long partner_id_customer_v2 = 2001410;

    public static final String test_key_customer_v2 = "ed4f1a9a419e4dd2dc33c2d791dc24995b862e9429c8138b62bbb5ce8eb74658";

    public static final long partner_id = 845376;

    public static final String partner_key = "76c4703a7ec5ef9a0d452097c653a6b1cf39d92db7706704dbb6a5c9563ad5b9";

    public static final String domain = "https://landing.vipage.vn";

//    public static final String domain = "http://localhost:6868";

    public static final String UPDATE_NOTE_ORDER_ENDPOINT = "https://partner.shopeemobile.com/api/v1/orders/note/add";
    static {
        MAPPER = MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    }


    public static final String CANCEL_ORDER_ENDPOINT = "https://partner.test-stable.shopeemobile.com/api/v2/order/cancel_order";
}
