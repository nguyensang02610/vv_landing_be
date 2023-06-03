package com.vvlanding.dto.shipped.viettel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginViettel {
    @JsonProperty("USERNAME")
    private String USERNAME;

    @JsonProperty("PASSWORD")
    private String PASSWORD;
}
