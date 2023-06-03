package com.vvlanding.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TotalOrderStatus {
    private int totalDM;
    private int totalDXN;
    private int totalGVC;
    private int totalDG;
    private int totalDaG;
    private int totalCH;
    private int totalDH;
    private int totalDDS;
    private int totalDelay;
    private int totalHuy;
    private int totalData;
    int month;
}
