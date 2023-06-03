package com.vvlanding.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "map_ghn")
public class MapGHN {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipped_id")
    private Shipped shipped;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ghn_Created_Order_id")
    private GHNOrderResponse ghnOrderResponse;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill;

}
