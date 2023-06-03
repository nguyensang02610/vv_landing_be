package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoShopInfo {
    private Long id;
    private String title;
    private String logo;
    private String address;
    private String phone;
    private String email;
    private String manager;
    private String telegram;
    private String youtube;
    private String website;
    private Long userId;
    private String userName;
    private String shopToken;
    private String district;
    private String province;
    private String ward;

}
