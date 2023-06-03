package com.vvlanding.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportBill {
    double totalMoney;
    double totalConfirmMoney;
    int totalBill;
    int totalConfirmBill;
    int month;
    double totalShipMoney;

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getTotalConfirmMoney() {
        return totalConfirmMoney;
    }

    public void setTotalConfirmMoney(double totalConfirmMoney) {
        this.totalConfirmMoney = totalConfirmMoney;
    }

    public int getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public int getTotalConfirmBill() {
        return totalConfirmBill;
    }

    public void setTotalConfirmBill(int totalConfirmBill) {
        this.totalConfirmBill = totalConfirmBill;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
