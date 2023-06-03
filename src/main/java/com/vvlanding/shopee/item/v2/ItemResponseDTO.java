package com.vvlanding.shopee.item.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDTO {
    private String error;
    private String message;
    private String warning;
    private String request_id;

    @JsonProperty("response")
    private ItemResponse response;

}
