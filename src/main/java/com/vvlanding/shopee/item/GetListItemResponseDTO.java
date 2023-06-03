package com.vvlanding.shopee.item;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class GetListItemResponseDTO {
    private List<ListItem> items = new ArrayList<>();
    private boolean more;
    private String request_id;
    private int total;
}
