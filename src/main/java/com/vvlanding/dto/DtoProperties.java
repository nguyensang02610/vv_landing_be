package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoProperties {
    private long id;
    private String keyname;
    private String value;
    private Date createdDate;
    private Date modifiedDate;

}
