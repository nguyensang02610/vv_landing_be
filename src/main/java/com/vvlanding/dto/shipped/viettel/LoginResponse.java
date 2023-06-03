package com.vvlanding.dto.shipped.viettel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {

    private Long userId;

    private String token;

    private Long partner;

    private String phone;

//    private String expired;
//
//    private String encrypted;
//
//    private String source;
}
