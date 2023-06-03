package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "landingPage")
public class LandingPage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "images")
    private String images;

    @Column(name = "codeLd")
    private String codeLd;

    @Column(name = "banner")
    private String banner;

    @ManyToOne
    @JoinColumn(name = "typeLd_id")
    private TypeLd typeLd;

    @JsonIgnore
    @OneToMany(mappedBy = "landingPage", fetch = FetchType.LAZY)
    private Set<RefLandingPageUser> refLandingPageUsers;

}
