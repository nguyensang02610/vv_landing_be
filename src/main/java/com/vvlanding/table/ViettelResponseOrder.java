package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "viettel_response_order")
public class ViettelResponseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("ORDER_NUMBER")
    @Column(name = "order_number")
    private String orderNumber;

    @JsonProperty("MONEY_COLLECTTION")
    @Column(name = "money_collection")
    private Double moneyCollection;

    @JsonProperty("EXCHANGE_WEIGHT")
    @Column(name = "exchange_weight")
    private Double exchangeWeight;

    @JsonProperty("MONEY_TOTAL")
    @Column(name = "money_total")
    private Double moneyTotal;

    @JsonProperty("MONEY_TOTAL_FEE")
    @Column(name = "money_total_fee")
    private Double moneyTotalFee;

    @JsonProperty("MONEY_FEE")
    @Column(name = "money_fee")
    private Double moneyFee;

    @JsonProperty("MONEY_COLLECTION_FEE")
    @Column(name = "money_collection_fee")
    private Double moneyCollectionFee;

    @JsonProperty("MONEY_OTHER_FEE")
    @Column(name = "money_other_fee")
    private Double moneyOtherFee;

    @JsonProperty("MONEY_VAS")
    @Column(name = "money_vas")
    private Double moneyVas;

    @JsonProperty("MONEY_VAT")
    @Column(name = "money_vat")
    private Double moneyVat;

    @JsonProperty("KPI_HT")
    @Column(name = "kpi_ht")
    private Double kpiHT;
}
