package com.vvlanding.shopee.webhook.shopee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseData {

    public long shop_id;
    public int code;
    public long timestamp;

    public Data data;

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        private String type;
        private ChatResponse content;
    }
}
