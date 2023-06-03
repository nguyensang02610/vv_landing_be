package com.vvlanding.shopee.item.v2.addProduct;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseImgProduct {
    private String error;
    private String message;
    private ImgShopee response;


    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ImgShopee{
        private String url;
        private String thumbnail;
        private ImageInfo image_info;

        @Getter
        @Setter
        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class ImageInfo{
            private String image_id;
        }
    }
}
