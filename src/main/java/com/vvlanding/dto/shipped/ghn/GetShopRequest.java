package com.vvlanding.dto.shipped.ghn;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetShopRequest {
    private int offset;
    private int limit;
    private String client_phone;
}
