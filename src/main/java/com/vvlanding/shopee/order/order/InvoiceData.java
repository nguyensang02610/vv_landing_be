package com.vvlanding.shopee.order.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceData {
    private String number;
    private String series_number;
    private String access_key;
    private double total_value;
    private double products_total_value;

}
