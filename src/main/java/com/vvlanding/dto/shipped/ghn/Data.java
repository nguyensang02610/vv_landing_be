package com.vvlanding.dto.shipped.ghn;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Data {
    private int last_offset;

    List<GHNGetShop> shops = new ArrayList<>();
}
