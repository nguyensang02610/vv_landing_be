package com.vvlanding.dto.shipped.ghn;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GHNGetShopDTO {

    private Long shopGhnId;

    private String nameShop;

    private int phone;

    private String address;
}

