package com.vvlanding.shopee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShopInforResponseDTO {
    private int shop_id;
    private String shop_name;
    private String shop_description;
    private String country;
    private String[] videos;
    private String[] images;
    private String status;
    private Object[] sip_a_shops;
    private Date auth_time;
    private Date expire_time;

}
