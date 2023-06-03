package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "titleLandingDetail")
public class TitleLandingDetail {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image")
    private String image;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "titleLandingDetails")
    @JsonIgnore
    private Set<TitleLanding> titleLandings = new HashSet<>();

    public TitleLandingDetail(String title, String content, String image, Long id) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.id = id;
    }

}
