package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoReview {
    private Long id;
    private String name;
    private String content;
    private String img;
    private Double view;
    private Long shopId;

    public void Review(Long id, String name, String content, String img, Long shopId, Double view) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.img = img;
        this.view = view;
        this.shopId = shopId;
    }
}
