package com.vvlanding.dto.shipped.viettel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViettelOrderDTO {

    @JsonProperty("ORDER_NUMBER")
    private String orderNumber;

    @JsonProperty("GROUPADDRESS_ID")
    private Long groupAddressId;

    @JsonProperty("CUS_ID")
    private Long cusId;

    @JsonProperty("DELIVERY_DATE")
    private String deliveryDate;

    @JsonProperty("SENDER_FULLNAME")
    private String senderFullName;

    @JsonProperty("SENDER_ADDRESS")
    private String senderAddress;

    @JsonProperty("SENDER_PHONE")
    private String senderPhone;

    @JsonProperty("SENDER_EMAIL")
    private String senderEmail;

    @JsonProperty("SENDER_WARD")
    private Long senderWard;

    @JsonProperty("SENDER_DISTRICT")
    private Long senderDistrict;

    @JsonProperty("SENDER_PROVINCE")
    private Long senderProvince;

    @JsonProperty("SENDER_LATITUDE")
    private Long senderLatitude;

    @JsonProperty("SENDER_LONGITUDE")
    private Long senderLongitude;

    @JsonProperty("RECEIVER_FULLNAME")
    private String receiverFullname;

    @JsonProperty("RECEIVER_ADDRESS")
    private String receiverAddress;

    @JsonProperty("RECEIVER_PHONE")
    private String receiverPhone;

    @JsonProperty("RECEIVER_EMAIL")
    private String receiverEmail;

    @JsonProperty("RECEIVER_WARD")
    private Long receiverWard;

    @JsonProperty("RECEIVER_DISTRICT")
    private Long receiverDistrict;

    @JsonProperty("RECEIVER_PROVINCE")
    private Long receiverProvince;

    @JsonProperty("RECEIVER_LATITUDE")
    private Long receiverLatitude;

    @JsonProperty("RECEIVER_LONGITUDE")
    private Long receiverLongitude;

    @JsonProperty("PRODUCT_NAME")
    private String produceName;

    @JsonProperty("PRODUCT_DESCRIPTION")
    private String productDescription;

    @JsonProperty("PRODUCT_QUANTITY")
    private Long productQuantity;

    @JsonProperty("PRODUCT_WEIGHT")
    private Long produceWeight;

    @JsonProperty("PRODUCT_PRICE")
    private Long productPrice;

    @JsonProperty("PRODUCT_LENGTH")
    private Long produceLength;

    @JsonProperty("PRODUCT_WIDTH")
    private Long productWidth;

    @JsonProperty("PRODUCT_HEIGHT")
    private Long productHeight;

    @JsonProperty("PRODUCT_TYPE")
    private String productType;

    @JsonProperty("ORDER_PAYMENT")
    private Long orderPayment;

    @JsonProperty("ORDER_SERVICE")
    private String orderService;

    @JsonProperty("ORDER_SERVICE_ADD")
    private String orderServiceAdd;

    @JsonProperty("ORDER_VOUCHER")
    private String orderVoucher;

    @JsonProperty("ORDER_NOTE")
    private String orderNote;

    @JsonProperty("MONEY_COLLECTION")
    private Long moneyCollection;

    @JsonProperty("MONEY_TOTALFEE")
    private Long moneyTotalFee;

    @JsonProperty("MONEY_FEECOD")
    private Long moneyFeeCod;

    @JsonProperty("MONEY_FEEVAS")
    private Long moneyFeeVas;

    @JsonProperty("MONEY_FEEINSURRANCE")
    private Long moneyFeeInsurrance;

    @JsonProperty("MONEY_FEE")
    private Long moneyFee;

    @JsonProperty("MONEY_FEEOTHER")
    private Long moneyFeeOther;

    @JsonProperty("MONEY_TOTALVAT")
    private Long moneyTotalVat;

    @JsonProperty("MONEY_TOTAL")
    private Long moneyTotal;

    @JsonProperty("LIST_ITEM")
    private List<ListProductOrder> listItem;
}
