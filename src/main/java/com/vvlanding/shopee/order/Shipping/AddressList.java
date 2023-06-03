package com.vvlanding.shopee.order.Shipping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressList {
    private String address_id;
    private String zipcode;
    private TimeSlotList[] time_slot_list;

}
