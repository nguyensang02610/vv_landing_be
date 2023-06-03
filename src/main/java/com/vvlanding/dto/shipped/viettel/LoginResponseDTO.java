package com.vvlanding.dto.shipped.viettel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponseDTO {
    private Long status;
    private boolean error;
    private String message;

    LoginResponse data;
}
