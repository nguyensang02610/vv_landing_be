package com.vvlanding.shopee.item.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoProductShopee {
    private String title;

    private Boolean isActive;

    private Double price;

    private Double saleprice;

    private String channel;

    private Long shopeeId;

    private Long itemId;

    private List<String> images;
}
