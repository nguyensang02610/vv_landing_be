package com.vvlanding.shopee.item.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDetailResponseDTO {
    private String error;
    private String message;
    private String warning;
    private String request_id;

    @JsonProperty("response")
    private ItemDetailResponse response;
}
