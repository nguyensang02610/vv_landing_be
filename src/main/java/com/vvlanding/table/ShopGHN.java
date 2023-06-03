package com.vvlanding.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shop_ghn")
public class ShopGHN {

    @Id
    @Column(name = "id", nullable = false, unique = false, length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ghn_id")
    private Long shopGhnId;

    @Column(name = "name")
    private String nameShop;

    @Column(name = "phone")
    private int phone;

    @Column(name = "address")
    private String address;

    @Column(name = "ward_code")
    private String wardCode;

    @Column(name = "district_id")
    private int districtId;

    @Column(name = "client_id")
    private int clientId;

    @Column(name = "status")
    private int status;

    @Column(name = "updated_client")
    private Date updatedClient;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipped_id")
    private Shipped shipped;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_info_id")
    private ShopInfo shopInfo;
}
