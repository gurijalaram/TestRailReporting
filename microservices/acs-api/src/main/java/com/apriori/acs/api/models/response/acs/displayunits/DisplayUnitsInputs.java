package com.apriori.acs.api.models.response.acs.displayunits;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisplayUnitsInputs {
    private String currencyCode;
    private String currencyLabel;
    private UnitVariantSettingsInfoInputs unitVariantSettingsInfo;
}
