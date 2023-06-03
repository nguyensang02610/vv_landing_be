package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoTypeLd {
    private Long id;
    private String title;
    private List<DtoLandingPage> landingPages = new ArrayList<>();

    public List<DtoLandingPage> getLandingPages() {
        return landingPages;
    }

    public void setLandingPages(List<DtoLandingPage> landingPages) {
        this.landingPages = landingPages;
    }

}
