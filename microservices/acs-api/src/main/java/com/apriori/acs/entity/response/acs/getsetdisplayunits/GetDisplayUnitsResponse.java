package com.apriori.acs.entity.response.acs.getsetdisplayunits;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GetDisplayUnitsResponse.json")
public class GetDisplayUnitsResponse {
    private String currencyCode;
    private String currencyLabel;
    private double currencyExchangeRate;
    private UnitVariantSettingsInfo unitVariantSettingsInfo;
}
