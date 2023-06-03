package com.vvlanding.dto;

import com.vvlanding.table.LandingPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoGetAllTypeLd {
    private Long id;
    private String title;
    private List<LandingPage> landingPage;

}
