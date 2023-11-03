package com.apriori.acs.api.models.response.acs.tolerancepolicydefaults;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/TolerancePolicyDefaultsResponse.json")
public class TolerancePolicyDefaultsResponse {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;
}
