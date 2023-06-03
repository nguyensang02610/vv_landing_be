package com.vvlanding.dto.shipped.viettel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FeeViettelDTO {

      @JsonProperty("PRODUCT_WEIGHT")
      private int PRODUCT_WEIGHT;

      @JsonProperty("PRODUCT_PRICE")
      private Double PRODUCT_PRICE;

      @JsonProperty("MONEY_COLLECTION")
      private int MONEY_COLLECTION;

      @JsonProperty("ORDER_SERVICE_ADD")
      private String ORDER_SERVICE_ADD;

      @JsonProperty("ORDER_SERVICE")
      private String ORDER_SERVICE;

      @JsonProperty("SENDER_PROVINCE")
      private String SENDER_PROVINCE;

      @JsonProperty("SENDER_DISTRICT")
      private String SENDER_DISTRICT;

      @JsonProperty("RECEIVER_PROVINCE")
      private String RECEIVER_PROVINCE;

      @JsonProperty("RECEIVER_DISTRICT")
      private String RECEIVER_DISTRICT;

      @JsonProperty("PRODUCT_TYPE")
      private String PRODUCT_TYPE;

      @JsonProperty("NATIONAL_TYPE")
      private int NATIONAL_TYPE;

      @JsonProperty("SENDER_PROVINCE_NAME")
      private String senderProvinceName;

      @JsonProperty("SENDER_DISTRICT_NAME")
      private String senderDistrictName;

      @JsonProperty("RECEIVER_PROVINCE_NAME")
      private String receiverProvinceName;

      @JsonProperty("RECEIVER_DISTRICT_NAME")
      private String receiverDistrictName;
}
