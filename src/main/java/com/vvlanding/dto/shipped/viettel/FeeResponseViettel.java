package com.vvlanding.dto.shipped.viettel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeeResponseViettel {

     @JsonProperty("MONEY_TOTAL_OLD")
     private Double MONEY_TOTAL_OLD;

     @JsonProperty("MONEY_TOTAL")
     private Double MONEY_TOTAL;

     @JsonProperty("MONEY_TOTAL_FEE")
     private Double MONEY_TOTAL_FEE;

     @JsonProperty("MONEY_FEE")
     private Double MONEY_FEE;

     @JsonProperty("MONEY_COLLECTION_FEE")
     private Double MONEY_COLLECTION_FEE;

     @JsonProperty("MONEY_OTHER_FEE")
     private Double MONEY_OTHER_FEE;

     @JsonProperty("MONEY_VAS")
     private Double MONEY_VAS;

     @JsonProperty("MONEY_VAT")
     private Double MONEY_VAT;

     @JsonProperty("KPI_HT")
     private Double KPI_HT;
}
