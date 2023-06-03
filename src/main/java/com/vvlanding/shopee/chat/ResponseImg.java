package com.vvlanding.shopee.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseImg {

    private String error;
    private String message;
    private ResponseUpdateImg response;


    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ResponseUpdateImg{
        private String url;
        private String thumbnail;
    }
}
