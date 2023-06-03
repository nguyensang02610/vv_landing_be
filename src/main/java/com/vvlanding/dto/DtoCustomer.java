package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoCustomer {
    private Long id;
    private String title;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;
    private boolean isActive;
    private String shopTitle;
    private Long shopId;
}
