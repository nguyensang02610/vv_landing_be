package com.vvlanding.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoPaymentOnePay {
    private Long id;

    private String name;

    private String hashCode;

    private String merchant;

    private String accessCode;

    private Long shopId;
}
