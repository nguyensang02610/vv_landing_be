package com.vvlanding.dto.shipped.ghn;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetShopDTO {
    private int code;

    private String message;

    private Data data;
}
