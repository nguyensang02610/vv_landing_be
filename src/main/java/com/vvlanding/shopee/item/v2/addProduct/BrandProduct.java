package com.vvlanding.shopee.item.v2.addProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandProduct {
    private String error;
    private String message;
    private String warning;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Response{
        private BrandList brand_list;
        private boolean has_next_page;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class BrandList{
            private Long brand_id;
            private String original_brand_name;
            private String display_brand_name;
        }
    }
}
