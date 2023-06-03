package com.vvlanding.dto.shipped.viettel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateOrderViettel {

    @JsonProperty("TYPE")
    private Double type;

    @JsonProperty("ORDER_NUMBER")
    private String orderNumber;

    @JsonProperty("NOTE")
    private String note;
}
