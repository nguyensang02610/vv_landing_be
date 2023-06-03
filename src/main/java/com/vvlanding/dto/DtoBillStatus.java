package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DtoBillStatus {
    private long id;

    private Long shopId;

    private String codeBill;

    private Double totalMoney;

    private Double shopShipMoney;

    private Double paidMoney;

    private Double discountPercent;

    private Double discountMoney;

    private Double cashMoney;

    private String shipPartner;

    private Double shipFee;

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

    public void Bill(Long id, Double totalMoney, String customerName, String customerPhone, Boolean isActive,
                     Date createdDate, String shipPartner, String status, String channel, String codeBill) {
        this.id = id;
        this.totalMoney = totalMoney;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.shipPartner = shipPartner;
        this.status = status;
        this.channel = channel;
        this.codeBill = codeBill;
    }

}
