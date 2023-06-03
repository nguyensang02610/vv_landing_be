package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoBillUpdate {

    private Long billId;

    private String customerName;

    private String customerPhone;

    private String customerProvince;

    private String customerDistrict;

    private String customerWard;

    private String customerFulladdress;

    private int weight;

    private Double totalMoney;

    private String shipFee;

    private String shopShipMoney;

    private Double cashMoney;

    private List<DtoOrderDetail> dtoBillDetail = new ArrayList<>();

}
