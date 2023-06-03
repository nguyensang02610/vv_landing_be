package com.vvlanding.dto.shipped.ghn;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GHNLog {
    private String status;
    private String updated_date;
}
