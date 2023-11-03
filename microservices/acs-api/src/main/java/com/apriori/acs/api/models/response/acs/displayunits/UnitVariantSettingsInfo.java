package com.apriori.acs.api.models.response.acs.displayunits;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UnitVariantSettingsInfo {
    @JsonProperty("@type")
    private String type;
    private String name;
    private String metric;
    private String length;
    private String mass;
    private String time;
    private int decimalPlaces;
    private boolean system;
    private boolean custom;
}
