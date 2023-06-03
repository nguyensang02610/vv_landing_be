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
public class DtoBillView {
    private long id;

    private String status;

    private Long shopId;

    private String customerName;

    private String customerPhone;

    private String customerProvince;

    private String customerDistrict;

    private Double totalMoney;

    private Boolean viewStatus;

    private Date createdDate;

    private String channel;

    public void Bill(Long id, String status, Double totalMoney, String title, String phone, String district, String province, Boolean viewStatus, Date createdDate, Long id1, String channel) {
        this.id = id;
        this.status = status;
        this.totalMoney = totalMoney;
        this.customerName = title;
        this.customerPhone = phone;
        this.customerDistrict = district;
        this.customerProvince = province;
        this.viewStatus = viewStatus;
        this.createdDate = createdDate;
        this.shopId = id1;
        this.channel = channel;
    }
}
