package com.vvlanding.dto;

import com.vvlanding.table.TitleLandingDetail;
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
public class DtoTitleLanding {
    private Long id;
    private String title;
    private String content;
    private String section;
    private List<TitleLandingDetail> titleLandingDetails = new ArrayList<>();
}
