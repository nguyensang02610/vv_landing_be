package com.vvlanding.reponse.message;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatusMessage {
    private boolean success;
    private String message;
    private Object data;
}
