package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoConfig {

    private Long id;
    private String title;
    private String description;
    private String keywords;
    private String phone;
    private String zalo;
    private String facebook;
    private Long refId;
    private String facebookPlug;
    private String ggAnalytics;
    private String plugOther;
    private Long ShopId;
}
