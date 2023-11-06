package com.apriori.acs.api.models.response.acs.displayunits;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/DisplayUnitsResponse.json")
public class DisplayUnitsResponse {
    private String currencyCode;
    private String currencyLabel;
    private double currencyExchangeRate;
    private UnitVariantSettingsInfo unitVariantSettingsInfo;
}
