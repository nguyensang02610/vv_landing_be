package com.vvlanding.dto.shipped;

import com.vvlanding.dto.DtoBill;
import com.vvlanding.table.Bill;
import lombok.*;

import java.util.List;
import java.util.function.BiConsumer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TotalBillDTO {

    private Double totalRevenue;
    private Double totalShipped;
    private Double money;

    private List<DtoBill> bills;
}
