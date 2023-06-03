package com.vvlanding.dto.shipped.viettel;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeeAddressDTO {

    private int status;

    private boolean error;

    private String message;

    @JsonProperty("data")
    List<FeeAddress> data;
}
