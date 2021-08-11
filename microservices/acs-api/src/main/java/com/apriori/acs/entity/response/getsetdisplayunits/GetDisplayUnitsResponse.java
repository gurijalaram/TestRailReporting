package com.apriori.acs.entity.response.getsetdisplayunits;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "GetDisplayUnitsResponse.json")
public class GetDisplayUnitsResponse {
    private String currencyCode;
    private String currencyLabel;
    private double currencyExchangeRate;
    private UnitVariantSettingsInfo unitVariantSettingsInfo;
}
