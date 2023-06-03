package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "titleLanding")
public class TitleLanding {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "section")
    private String section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refLandingPageUser_id")
    private RefLandingPageUser refLandingPageUser1;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })

    @JoinTable(name = "ref_titleLanding_titleDetail",
            joinColumns = @JoinColumn(
                    name = "titleLanding_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "titleDetail_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<TitleLandingDetail> titleLandingDetails;

}
