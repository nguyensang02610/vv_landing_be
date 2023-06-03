package com.vvlanding.dto.shipped.ghn;

import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GHNGetShop {

    private Long _id;

    private String name;

    private int phone;

    private String address;

    private String ward_code;

    private int district_id;

    private int client_id;

    private int status;

    private Date updated_client;

    private Date created_date;

    private int bank_account_id;

    private String version_no;

    private String updated_ip;

    private int updated_employee;

    private String updated_source;

}
