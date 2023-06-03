package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GHNOrder {
	@JsonProperty("to_name")
	private String toName;

	@JsonProperty("to_phone")
	private String toPhone;

	@JsonProperty("to_address")
	private String toAddress;

	@JsonProperty("to_ward_code")
	private String toWardCode;

	@JsonProperty("to_district_id")
	private int toDistrictId;

	@JsonProperty("return_phone")
	private String returnPhone;

	@JsonProperty("return_address")
	private String returnAddress;

	@JsonProperty("return_district_id")
	private int returnDistrictId;

	@JsonProperty("shop_id")
	private String shopId;

	@JsonProperty("return_ward_code")
	private String returnWardCode;

	@JsonProperty("client_order_code")
	private String clientOrderCode;

	@JsonProperty("cod_amount")
	private int codAmount;

	private String content;
	private int weight;
	private int length;

	@JsonProperty("pick_station_id")
	private int pickStationId;

	@JsonProperty("insurance_value")
	private int insuranceValue;

	private String coupon;

	@JsonProperty("service_type_id")
	private int serviceTypeId;

	@JsonProperty("service_id")
	private int serviceId;

	@JsonProperty("payment_type_id")
	private int paymentTypeId;

	private String note;

	@JsonProperty("required_note")
	private String requiredNote;

	private String order_code;

	private List<GHNProduct> items;
}
