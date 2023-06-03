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
@Table(name = "payment")
public class Payment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name") // tên cổng thanh toán
    private String name;

    // onePay - hashCode
    @Column(name = "value1")
    private String value1;

    // onePay - merchant
    @Column(name = "value2")
    private String value2;

    // onePay - access_code
    @Column(name = "value3")
    private String value3;

    @Column(name = "value4")
    private String value4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopInfo_id")
    private ShopInfo shopInfo;
}
