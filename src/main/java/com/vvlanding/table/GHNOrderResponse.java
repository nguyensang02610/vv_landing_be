package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ghn_created_order")
public class GHNOrderResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "expected_delivery_time")
    @JsonProperty("expected_delivery_time")
    private String expectedDeliveryTime;

    @Column(name = "order_code")
    @JsonProperty("order_code")
    private String orderCode;

    @Column(name = "sort_code")
    @JsonProperty("sort_code")
    private String sortCode;

    @Column(name = "total_fee")
    @JsonProperty("total_fee")
    private String totalFee;

    @Column(name = "trans_type")
    @JsonProperty("trans_type")
    private String transType;

    @Column(name = "ward_encode")
    @JsonProperty("ward_encode")
    private String wardEncode;

}
