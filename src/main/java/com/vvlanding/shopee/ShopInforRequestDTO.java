package com.vvlanding.shopee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShopInforRequestDTO {

    @JsonProperty("shopid")
    private long shopid;

    @JsonProperty("partner_id")
    private long partnerId;


    @JsonProperty("access_token")
    private String accessToken;


    @JsonProperty("timestamp")
    private long timestamp;
}
