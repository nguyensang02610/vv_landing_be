package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GHNOrderDetail {

//    private int shop_id;
//
    private int client_id;
//
    @JsonProperty("to_name")
    private String toName;

    @JsonProperty("to_phone")
    private String toPhone;

    @JsonProperty("to_address")
    private String toAddress;

    @JsonProperty("to_ward_code")
    private String toWardCode;

    @JsonProperty("to_district_id")
    private int toDistrictId;

    @JsonProperty("return_phone")
    private String returnPhone;

    @JsonProperty("return_address")
    private String returnAddress;

    @JsonProperty("return_district_id")
    private int returnDistrictId;

    @JsonProperty("shop_id")
    private String shopId;

    @JsonProperty("return_ward_code")
    private String returnWardCode;

    @JsonProperty("client_order_code")
    private String clientOrderCode;

    @JsonProperty("cod_amount")
    private int codAmount;

    private String content;
    private int weight;
    private int length;

    @JsonProperty("pick_station_id")
    private int pickStationId;

    @JsonProperty("insurance_value")
    private int insuranceValue;

    private String coupon;

    @JsonProperty("service_type_id")
    private int serviceTypeId;

    @JsonProperty("service_id")
    private int serviceId;

    @JsonProperty("payment_type_id")
    private int paymentTypeId;

    private String note;

    @JsonProperty("required_note")
    private String requiredNote;

    private String return_name;
    private String from_name;
    private String from_phone;
    private String from_address;
    private String from_ward_code;
    private int from_district_id;
    private int deliver_station_id;

    private int converted_weight;
    private int custom_service_fee;
    private String cod_collect_date;
    private String cod_transfer_date;
    private String is_cod_transferred;
    private String is_cod_collected;
    private int order_value;
    private String employee_note;
    private String _id;
    private String order_code;
    private String version_no;
    private String updated_ip;
    private int updated_employee;
    private int updated_client;
    private String updated_source;
    private String updated_date;
    private int updated_warehouse;
    private String created_ip;
    private int created_employee;
    private int created_client;
    private String created_source;
    private String created_date;
    private String status;
    private int pick_warehouse_id;
    private int deliver_warehouse_id;
    private int current_warehouse_id;
    private int return_warehouse_id;
    private int next_warehouse_id;
    private String leadtime;
    private int[] payment_type_ids;
    private FormLocation from_location;
    private ToLocation to_location;
    private String order_date;
    private String soc_id;
    private String finish_date;
    private List<String> tag;
    private List<GHNLog> log;
    private boolean is_partial_return;

}
