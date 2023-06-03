package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GHNFeeDTO {
	private int code;
	private String msg;

	@JsonProperty("data")
	private GHNFee ghnFee;
}
