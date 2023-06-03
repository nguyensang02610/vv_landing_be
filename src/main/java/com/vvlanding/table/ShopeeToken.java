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
@Table(name = "shopee_token")
public class ShopeeToken {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shopee_id")
    private Long shopeeId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "refresh_token")
    private String refresh_token;

    @Column(name = "access_token")
    private String access_token;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "app_id")
    private Long appId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_info_id")
    ShopInfo shopInfo;
}
