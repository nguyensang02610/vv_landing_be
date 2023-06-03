package com.vvlanding.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name") // tên người review
    private String name;

    @Column(name = "content", columnDefinition = "TEXT") //nội dung
    private String content;

    @Column(name = "img") //ảnh
    private String img;

    @Column(name = "view") //lượt xem
    private Double view;

    // shop 1 - n review
    @ManyToOne
    @JoinColumn(name = "shopInfo_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShopInfo shopInfo;

}
