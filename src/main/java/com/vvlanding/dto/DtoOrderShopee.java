package com.vvlanding.dto;

import com.vvlanding.table.Bill;
import com.vvlanding.table.Product;
import com.vvlanding.table.Properties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class DtoOrderShopee {

    private Long id;

    private String ordersn;

    private Date updateTime;

    private String orderStatus;

    private String buyerUserId;

    private String note;

    private Double shopShipMoney;

    private List<ResponseBillDetail> billDetail;

    private Date createDate;

}
