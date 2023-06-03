package com.vvlanding.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private ShopInfo shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bills")
    private Set<BillDetail> billDetails;

    @Column(name = "code_bill") // mã đơn hàng
    private String codeBill;

    @Column(name = "order_code") // mã order đơn vị vận chuyển
    private String orderCode;

    @Column(name = "total_money") // tổng tiền hàng
    private Double totalMoney;

    @Column(name = "shop_ship_money") // tổng tiền khách phải trả = tiền hàng + ship
    private Double shopShipMoney;

    @Column(name = "paid_money") // tiền đã thanh toán
    private Double paidMoney;

    @Column(name = "discount_percent") // phần trăm giảm giá
    private Double discountPercent;

    @Column(name = "discount_money") // tiền giảm giá
    private Double discountMoney;

    @Column(name = "cash_money") // số tiền bên ship thu hộ ( tiền hàng + tiền ship)
    private Double cashMoney;

    @Column(name = "inner_note") //  ghi chú của khách hàng
    private String innerNote;

    @Column(name = "print_note") //chú thích CHOXEMHANGKHONGTHU , CHOTHUHANG
    private String printNote;

    @Column(name = "weight") // cân nặng
    private int weight;

    @Column(name = "ship_fee") //tiền ship
    private Double shipFee;

    @Column(name = "ship_partner") // tên đơn vị vận chuyển
    private String shipPartner;

    @Column(name = "ship_type") // người thanh toán ( 2 - người nhận trả )
    private String shipType;

    @Column(name = "status") // trạng thái đơn
    private String status;

    @Column(name = "viewStatus") // trạng thái xem đơn
    private Boolean viewStatus;

    @Column(name = "created_date") // ngày tạo đơn
    @Temporal(TemporalType.TIMESTAMP)
    Date createdDate = new Date();

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "channel") // kênh bán
    private String channel;

    @Column(name = "ipaddress") //ip
    private String ipAddress;

    @Column(name = "payment_id")
    private String paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_shipped_id")
    private StatusShipped statusShipped;

}
