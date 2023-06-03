package com.vvlanding.dto.shipped.ghn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GHNOrderDetailDTO {
    private int code;
    private String message;
    private GHNOrderDetail data;
}