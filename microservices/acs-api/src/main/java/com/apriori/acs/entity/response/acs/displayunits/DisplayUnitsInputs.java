package com.apriori.acs.entity.response.acs.displayunits;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisplayUnitsInputs {
    private String currencyCode;
    private String currencyLabel;
    private UnitVariantSettingsInfoInputs unitVariantSettingsInfo;
}