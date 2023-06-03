package com.vvlanding.dto.shipped.ghtk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DtoGhtkStatusResponse {

    @JsonProperty("label_id")
    private String label_id;

    @JsonProperty("partner_id")
    private String partner_id;
    private String status;

    @JsonProperty("order_id")
    private String order_id;

    @JsonProperty("status_text")
    private String status_text;
    private String created;
    private String modified;
    private String message;

    @JsonProperty("pick_date")
    private String pick_date;

    @JsonProperty("deliver_date")
    private String deliver_date;

    @JsonProperty("customer_fullname")
    private String customer_fullname;

    @JsonProperty("customer_tel")
    private String customer_tel;
    private String address;

    @JsonProperty("storage_day")
    private String storage_day;

    @JsonProperty("ship_money")
    private String ship_money;
    private String insurance;
    private String value;
    private String weigh;

    @JsonProperty("pick_money")
    private String pick_money;

    @JsonProperty("is_freeship")
    private String is_freeship;
}
