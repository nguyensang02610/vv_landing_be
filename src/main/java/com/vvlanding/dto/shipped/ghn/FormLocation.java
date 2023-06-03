package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FormLocation {
     private int lat;
     @JsonProperty("long")
     private int longs;
     private String cell_code;
     private String place_id;
     private int trust_level;
     private int untrusted_reason;
}
