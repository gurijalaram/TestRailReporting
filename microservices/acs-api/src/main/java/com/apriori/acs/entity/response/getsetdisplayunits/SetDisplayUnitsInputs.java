package com.apriori.acs.entity.response.getsetdisplayunits;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SetDisplayUnitsInputs {
    private String currencyCode;
    private String currencyLabel;
    private UnitVariantSettingsInfoInputs unitVariantSettingsInfo;
}
