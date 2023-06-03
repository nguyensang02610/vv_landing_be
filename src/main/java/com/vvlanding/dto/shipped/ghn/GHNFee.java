package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GHNFee {

	@JsonProperty("Coupon")
	private int Coupon;

	@JsonProperty("Insurance")
	private int Insurance;

	@JsonProperty("MainService")
	private int MainService;

	@JsonProperty("R2S")
	private int R2S;

	@JsonProperty("Return")
	private int Return;

	@JsonProperty("StationDO")
	private int StationDO;
	@JsonProperty("StationPU")
	private int StationPU;

}
