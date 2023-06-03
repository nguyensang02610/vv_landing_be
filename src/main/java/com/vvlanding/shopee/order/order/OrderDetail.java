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
public class OrderDetail {

    String ordersn;

    @JsonProperty("order_sn")
    String order_sn;

    String country;
    String currency;

    boolean cod;
    String tracking_no;
    int days_to_ship;

    RecipientAddress recipient_address;
    double estimated_shipping_fee;  // phí vận chuyển ước tính
    double actual_shipping_cost;   // phí vận chuyển thực tế
    double total_amount; // tổng số tiền người mua đã trả
    double escrow_amount; // tổng số tiền mà người bán dự kiến sẽ nhận được cho đơn đặt hàng
    int weight;
    long variation_quantity_purchased;
    double variation_original_price;
    String order_status;
    String shipping_carrier;
    String payment_method;
    boolean goods_to_declare;
    String message_to_seller;
    String note;
    long note_update_time;
    long create_time;
    long update_time;

    List<ItemOrder> items = new ArrayList<>();

    @JsonProperty("item_list")
    List<ItemOrder> item_list = new ArrayList<>();
    InvoiceData invoice_data;
    int pay_time;
    String dropshipper;
    String credit_card_number;
    String buyer_username;
    String dropshipper_phone;
    int buyer_user_id;
    long ship_by_date;
    boolean is_split_up;
    String buyer_cancel_reason;
    String cancel_by;
    String fm_tn;
    String cancel_reason;

    double escrow_tax;
    boolean is_actual_shipping_fee_confirmed;
}
