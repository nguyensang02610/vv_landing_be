package com.vvlanding.shopee.order.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecipientAddress {
    private String name;
    private String phone;
    private String town;
    private String district;
    private String city;
    private String state;
    private String country;
    private String region;
    private String zipcode;
    private String full_address;
}
