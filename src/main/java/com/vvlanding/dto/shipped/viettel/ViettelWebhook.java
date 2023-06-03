package com.vvlanding.dto.shipped.viettel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViettelWebhook {

    @JsonProperty("ORDER_NUMBER")
    private String orderNumber;

    @JsonProperty("ORDER_REFERENCE")
    private String orderReference;

    @JsonProperty("ORDER_STATUSDATE")
    private String orderStatusDate;

    @JsonProperty("ORDER_STATUS")
    private int orderStatus;

    @JsonProperty("STATUS_NAME")
    private String statusName;

    @JsonProperty("LOCALION_CURRENTLY")
    private String localionCurrently;

    @JsonProperty("NOTE")
    private String note;

    @JsonProperty("MONEY_COLLECTION")
    private Double moneyCollection;

    @JsonProperty("MONEY_FEECOD")
    private Double moneyFeeCod;

    @JsonProperty("MONEY_TOTAL")
    private Double moneyTotal;

    @JsonProperty("EXPECTED_DELIVERY")
    private String expectedDelivery;

    @JsonProperty("PRODUCT_WEIGHT")
    private int productWeight;

    @JsonProperty("ORDER_SERVICE")
    private String orderService;
}
