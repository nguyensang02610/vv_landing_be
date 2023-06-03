package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shopInfo")
public class ShopInfo {
    @Id
    @Column(name = "id", nullable = false, unique = false, length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "logo")
    private String logo;

    @Column(name = "manager")
    private String manager;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "telegram")
    private String telegram;

    @Column(name = "youtube")
    private String youtube;

    @Column(name = "website")
    private String website;

    @Column(name = "district")
    private String district;

    @Column(name = "province")
    private String province;

    @Column(name = "ward")
    private String ward;
//
//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shop_user_role_id")
//    private ShopUserRole shopUserRole;

    @Column(name = "shop_token", columnDefinition = "TEXT")
    private String shopToken;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @OneToMany(mappedBy = "shopInfo",fetch = FetchType.LAZY)
    private Set<Payment> payments;

    @JsonIgnore
    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private Set<Bill> bills;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shopInfo")
    private Set<Product> products;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shopInfo")
    private Set<RefLandingPageUser> refLandingPageUsers;

}
