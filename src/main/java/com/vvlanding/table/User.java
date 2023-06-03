package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

@Table(name = "User", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "user_name"
        })
})
public class User {
    @Id
    @Column(name = "id", nullable = false, unique = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "image")
    private String image;

    @Column(name = "fb_id")
    private String fbId;

//    @JsonIgnore
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
//    private Set<ShopInfo> shopInfos;

    public User(String image, String username, String password, Boolean active, String phone, String email, String title) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.phone = phone;
        this.email = email;
        this.title = title;
        this.image = image;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<RefLandingPageUser> refLandingPageUsers;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<ShopUserRole> shopUserRoles;

    public User(long l, String userName, String email, String password, String title, boolean b, String phone, String image) {
        this.id = l;
        this.username = userName;
        this.password = password;
        this.active = b;
        this.phone = phone;
        this.email = email;
        this.title = title;
        this.image = image;
    }
}
