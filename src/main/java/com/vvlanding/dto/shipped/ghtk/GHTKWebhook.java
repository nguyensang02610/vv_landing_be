package com.vvlanding.dto.shipped.ghtk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GHTKWebhook {

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("label_id")
    private String labelId;

    @JsonProperty("status_id")
    private Integer status_id;

    @JsonProperty("action_time")
    private String actionTime;

    @JsonProperty("reason_code")
    private String reasonCode;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("weight")
    private Double weight;

    @JsonProperty("fee")
    private Integer fee;

    @JsonProperty("pick_money")
    private Double pickMoney;

    @JsonProperty("return_part_package")
    private Integer returnPartPackage;
}
