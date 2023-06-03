package com.vvlanding.dto;

import com.vvlanding.dto.shipped.DtoOrderProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoBill {
    private long id;

    private Long shopId;

    private String shopName;

    private String shopPhone;

    private String shopProvince;

    private String shopDistrict;

    private String shopWard;

    private String shopFulladdress;

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

    private Boolean viewStatus;

    private Date createdDate;

    private String channel;

    private String statusName;

    private String ipAddress;

    private String orderCode;

    private List<DtoOrderDetail> orderDetails = new ArrayList<>();

    private List<DtoOrderProduct> dtoOrderProducts = new ArrayList<>();


}
