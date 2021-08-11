package com.apriori.acs.entity.response.getunitvariantsettings;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(location = "GetCustomUnitVariantSettingsResponse.json")
public class UnitVariantSetting {
    @JsonProperty("@type")
    private String type;
    private String name;
    private String metric;
    private String length;
    private String mass;
    private String time;
    private double decimalPlaces;
    private boolean system;
    private boolean custom;
}
