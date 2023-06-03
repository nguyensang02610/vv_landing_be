package com.vvlanding.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_shopee")
public class OrderShopee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shopee")
    private String name;

    @Column(name = "ordersn")
    private String ordersn;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "phone")
    private String phone; // sdt người mua

    @Column(name = "tracking_no")
    private String trackingNo;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "buyer_user_id")
    private String buyerUserId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_shopee_shop_id")
    private ChannelShopee channelShopee;
}
