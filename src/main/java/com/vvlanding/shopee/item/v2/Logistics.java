package com.vvlanding.shopee.item.v2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Logistics {
    private Long logistics_channel_id;
    private String logistics_channel_name;
    private boolean cod_enabled;
    private boolean enabled;
    private String fee_type;
}
