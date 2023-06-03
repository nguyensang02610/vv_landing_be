package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoTitleLandingPage {
    private Long id;
    private String content;
    private int section;
    private int sub_section;
    private int kind;
}
