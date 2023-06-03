package com.vvlanding.shopee.webhook.shopee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPush {
    private int code;
    @JsonProperty("shop_id")
    private Long shop_id;
    private long timestamp;
}
