package com.vvlanding.shopee.item;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ItemDetailsResponseDTO {
    private Item item;
    private String request_id;
    private String  warning;
}
