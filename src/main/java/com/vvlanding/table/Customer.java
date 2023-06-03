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
@Table(name = "customer")// thông tin khách hàng
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")// tên
    private String title;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "province") // thành phố
    private String province;

    @Column(name = "district")// quận
    private String district;

    @Column(name = "ward")// phường xã
    private String ward;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Bill> bill;

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "is_active")
    private boolean isActive;

}
