package com.apriori.acs.entity.response.acs.displayunits;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/DisplayUnitsResponse.json")
public class DisplayUnitsResponse {
    private String currencyCode;
    private String currencyLabel;
    private double currencyExchangeRate;
    private UnitVariantSettingsInfo unitVariantSettingsInfo;
}
