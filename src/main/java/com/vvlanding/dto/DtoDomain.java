package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoDomain {
    private Long id;
    private String url;
    private Boolean status;
    private Date dateCreated;
    private Long ShopId;
    private String ShopTitle;
}
