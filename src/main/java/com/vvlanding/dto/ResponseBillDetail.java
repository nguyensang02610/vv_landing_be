package com.vvlanding.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResponseBillDetail {
    private Double quantity;
    private Double price;
    private Double money;
    private String productName;
    private String image;
    private String properties;
}
