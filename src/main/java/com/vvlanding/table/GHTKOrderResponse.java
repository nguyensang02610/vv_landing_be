package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vvlanding.dto.shipped.ghtk.DtoGHTKProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ghtk_created_order")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GHTKOrderResponse {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "partner_id")
    @JsonProperty("partner_id")
    private String partnerID;

    private String label;

    private String area;

    private String fee;

    @Column(name = "insurance_fee")
    @JsonProperty("insurance_fee")
    private String insuranceFee;

    @Column(name = "estimated_pick_time")
    @JsonProperty("estimated_pick_time")
    private String estimatedPickTime;

    @Column(name = "estimated_deliver_time")
    @JsonProperty("estimated_deliver_time")
    private String estimatedDeliverTime;

    @Transient
    private List<DtoGHTKProduct> products;

    @Column(name = "status_id")
    @JsonProperty("status_id")
    private int statusId;

}
