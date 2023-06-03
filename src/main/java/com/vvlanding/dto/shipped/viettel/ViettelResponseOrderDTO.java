package com.vvlanding.dto.shipped.viettel;

import com.vvlanding.table.ViettelResponseOrder;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViettelResponseOrderDTO {

    private int status;

    private boolean error;

    private String message;

    private ViettelResponseOrder data;
}
