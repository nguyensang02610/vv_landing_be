package com.vvlanding.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BillResp {
    int month;
    int countBill;

    public int getCountBill() {
        return countBill;
    }

    public int getMonth() {
        return month;
    }

    public void setCountBill(int countBill) {
        this.countBill = countBill;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
