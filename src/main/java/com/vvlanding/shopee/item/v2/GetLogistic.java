package com.vvlanding.shopee.item.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLogistic {
    private Long logistics_channel_id;
    private boolean preferred;
    private String logistics_channel_name;
    private boolean cod_enabled;
    private boolean enabled;
    private String fee_type;
    private String logistics_description;
    private boolean force_enable;
    private Long mask_channel_id;
}

