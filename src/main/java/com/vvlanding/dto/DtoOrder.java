package com.vvlanding.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DtoOrder {
    private long id;

    private Long shopId;

    private Long productId;

    private String codeBill;

    private Double totalMoney;

    private Double shopShipMoney;

    private Double paidMoney;

    private Double discountPercent;

    private Double discountMoney;

    private Double cashMoney;

    private String shipPartner;

    private String shipCode;

    private String shipFee;

    private String shipType;

    private String innerNote;

    private String printNote;

    private int weight;

    private String status;

    private String customerName;

    private String customerPhone;

    private String customerProvince;

    private String customerDistrict;

    private String customerWard;

    private String customerFulladdress;

    private Boolean isActive;

    private Date createdDate;

    private String channel;

    private Boolean viewStatus;

    private String ipAddress;

    private List<DtoOrderDetail> orderDetails = new ArrayList<>();

}
