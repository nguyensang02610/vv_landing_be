package com.vvlanding.dto.shipped.ghtk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GHTKFeeDTO {
    private boolean success;

    private String message;

    private GHTKFee fee;
}
