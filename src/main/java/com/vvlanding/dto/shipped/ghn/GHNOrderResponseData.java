package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GHNOrderResponseData {

	@JsonProperty("order_code")
	private String orderCode;

	@JsonProperty("expected_delivery_time")
	private String expectedDeliveryTime;

	@JsonProperty("total_fee")
	private String totalFee;

	@JsonProperty("trans_type")
	private String transType;

	@JsonProperty("ward_encode")
	private String wardEncode;

	@JsonProperty("district_encode")
	private String districtEncode;

	@JsonProperty("sort_code")
	private String sortCode;

	private GHNFee fee;

}
