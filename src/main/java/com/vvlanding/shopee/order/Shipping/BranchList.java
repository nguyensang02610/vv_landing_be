package com.vvlanding.shopee.order.Shipping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchList {
    private Long branch_id;
    private String region;
    private String state;
    private String city;
    private String address;
    private String zipcode;
    private String district;
    private String town;
}
