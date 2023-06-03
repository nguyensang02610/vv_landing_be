package com.vvlanding.dto.shipped.ghtk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vvlanding.table.GHTKOrderResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GHTKOrderResponseDTO {
    private boolean success;

    private String message;

    GHTKOrderResponse order;
}
