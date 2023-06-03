package com.vvlanding.dto.shipped.ghtk;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DtoGHTKOrder {
    private List<DtoGHTKProduct> products;
    private DtoGHTK order;
}
