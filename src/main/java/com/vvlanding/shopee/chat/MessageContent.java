package com.vvlanding.shopee.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContent {
    private String text;
    private String sticker_id;
    private String sticker_package_id;
    private String image_url;
    private String url;
    private String content;
    private int file_server_id;
    private int shop_id;
    private String order_sn;
    private Long item_id;
}
