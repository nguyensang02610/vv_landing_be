package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "channel_shopee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelShopee{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    @JsonIgnore
    private ShopInfo shop;

    @Column(name = "shopee_shop_id")
    private long shopeeShopId;

    @Column(name = "shopee_shop_name")
    private String shopeeShopName;

    @Column(name = "shopee_shop_desc", columnDefinition = "TEXT")
    private String shopeeShopDesc;

    @Column(name = "shopee_shop_image")
    private String shopeeShopImage;

    @Column(name = "region")
    private String region;

    @Column(name = "status")
    private String status;

    @Column(name = "sip_affi_shops")
    private String shipAffiShops;


}
