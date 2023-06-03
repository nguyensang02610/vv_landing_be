package com.vvlanding.dto.shipped.ghn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GHNWebhook {
    @JsonProperty("CODAmount")
    private Double CODAmount;

    @JsonProperty("CODTransferDate")
    private String CODTransferDate;

    @JsonProperty("ClientOrderCode")
    private String ClientOrderCode;

    @JsonProperty("ConvertedWeight")
    private int ConvertedWeight;

    @JsonProperty("Description")
    private String Description;

    @JsonProperty("Fee")
    private GHNFee Fee;

    @JsonProperty("Height")
    private int Height;

    @JsonProperty("Length")
    private int Length;

    @JsonProperty("OrderCode")
    private String OrderCode;

    @JsonProperty("Reason")
    private String Reason;

    @JsonProperty("ReasonCode")
    private String ReasonCode;

    @JsonProperty("ShipperName")
    private String ShipperName;

    @JsonProperty("ShipperPhone")
    private String ShipperPhone;

    @JsonProperty("Status")
    private String Status;

    @JsonProperty("Time")
    private String Time;

    @JsonProperty("TotalFee")
    private Double TotalFee;

    @JsonProperty("Type")
    private String Type;

    @JsonProperty("Warehouse")
    private String Warehouse;

    @JsonProperty("Weight")
    private int Weight;

    @JsonProperty("Width")
    private int Width;
}
