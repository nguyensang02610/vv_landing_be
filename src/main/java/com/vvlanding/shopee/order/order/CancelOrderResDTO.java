package com.vvlanding.shopee.order.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelOrderResDTO {
    String modified_time;
    String request_id;
    String msg;
    String error_param;
    String error_not_found;
    String error_server;
    String error_auth;
}