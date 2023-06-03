package com.vvlanding.dto.shipped.viettel;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeeResponseViettelDTO {

    private int status;

    private boolean error;

    private String message;

    private FeeResponseViettel data;
}
