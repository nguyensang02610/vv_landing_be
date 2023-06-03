package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoLandingPage {
    private Long id;
    private String title;
    private String images;
    private String codeLd;
    private String typeld;
    private String banner;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCodeLd() {
        return codeLd;
    }

    public void setCodeLd(String codeLd) {
        this.codeLd = codeLd;
    }

    public String getTypeld() {
        return typeld;
    }

    public void setTypeld(String typeld) {
        this.typeld = typeld;
    }
}
