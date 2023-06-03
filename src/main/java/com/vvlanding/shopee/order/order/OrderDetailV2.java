package com.vvlanding.shopee.order.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderDetailV2 {

    private String order_sn;

    private String region;

    private String currency;

    private boolean cod;

    private double total_amount;

    private String order_status;

    @JsonProperty("shipping_carrier")
    private String shipping_carrier;

    private String payment_method;

    private double estimated_shipping_fee;

    private String message_to_seller;

    private Long create_time;

    private Long update_time;

    private Long days_to_ship;

    private Long ship_by_date;

    private int buyer_user_id;

    private RecipientAddress recipient_address;

    private double actual_shipping_fee;

    private boolean goods_to_declare;

    private String note;

    private List<ItemOrder> item_list = new ArrayList<>();

    private List<PackageList> package_list = new ArrayList<>();

}
