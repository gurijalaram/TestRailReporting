package com.apriori.acs.models.response.acs.unitvariantsettings;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(location = "acs/CustomUnitVariantSettingsResponse.json")
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
