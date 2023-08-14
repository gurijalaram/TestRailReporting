package com.apriori.acs.models.response.acs.displayunits;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnitVariantSettingsInfoInputs {
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
