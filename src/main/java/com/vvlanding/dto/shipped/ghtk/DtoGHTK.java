package com.vvlanding.dto.shipped.ghtk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DtoGHTK {
    // Thông tin điểm lấy hàng
    @NotNull
    private String id;

    @NotNull
    @JsonProperty("pick_name")
    private String pickName;

    @NotNull
    @JsonProperty("pick_money")
    private Integer pickMoney;

    @JsonProperty("pick_address_id")
    private String pickAddressId;

    @NotNull
    @JsonProperty("pick_address")
    private String pickAddress;

    @NotNull
    @JsonProperty("pick_province")
    private String pickProvince;

    @NotNull
    @JsonProperty("pick_district")
    private String pickDistrict;

    @JsonProperty("pick_ward")
    private String pickWard;

    @JsonProperty("pick_session")
    private String pickSession;

    @JsonProperty("pick_street")
    private String pickStreet;

    @NotNull
    @JsonProperty("pick_tel")
    private String pickTel;

    @JsonProperty("pick_email")
    private String pickEmail;

    // Thông tin điểm giao hàng
    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String province;

    @NotNull
    private String district;

    private String ward;

    private String street;

    private String hamlet;

    @NotNull
    private String tel;

    private String note;

    private String email;

//     Thông tin điểm trả hàng

    @JsonProperty("use_return_address")
    private Integer useReturnAddress;

    @JsonProperty("return_name")
    private String returnName;

    @JsonProperty("return_address")
    private String returnAddress;

    @JsonProperty("return_province")
    private String returnProvince;

    @JsonProperty("return_district")
    private String returnDistrict;

    @JsonProperty("return_ward")
    private String returnWard;

    @JsonProperty("return_street")
    private String returnStreet;

    @JsonProperty("return_tel")
    private String returnTel;

    @JsonProperty("return_email")
    private String returnEmail;

    // Thông tin thêm

    @JsonProperty("deliver_option")
    private String deliverOption;

    @JsonProperty("is_freeship")
    private Integer isFreeship;

    @JsonProperty("weight_option")
    private String weightOption;

    @JsonProperty("total_weight")
    private Double totalWeight;

    @JsonProperty("pick_work_shift")
    private Integer pickWorkShift;

    @JsonProperty("deliver_work_shift")
    private Integer deliverWorkShift;

    @JsonProperty("label_id")
    private String labelId;

    @JsonProperty("pick_date")
    private String pickDate;

    @JsonProperty("deliver_date")
    private String deliverDate;

    private String expired;

    private Integer value;

    private Integer opm;

    @JsonProperty("pick_option")
    private String pickOption;

    @JsonProperty("actual_transfer_method")
    private String actualTransferMethod;

    private String transport;

    @JsonProperty("partner_id")
    private String partnerId;

    private String label;
    private String area;
    private String fee;
    @JsonProperty("insurance_fee")
    private String insuranceFee;

    @JsonProperty("estimated_pick_time")
    private String estimatedPickTime;

    @JsonProperty("estimated_deliver_time")
    private String estimatedDeliverTime;

    @JsonProperty("status_id")
    private String statusId;

    private DtoGHTKProduct product;
}
