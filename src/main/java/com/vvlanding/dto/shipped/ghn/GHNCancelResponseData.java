package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GHNCancelResponseData {

    @JsonProperty("order_code")
    private String orderCode;

    private Boolean result;

    private String message;
}
