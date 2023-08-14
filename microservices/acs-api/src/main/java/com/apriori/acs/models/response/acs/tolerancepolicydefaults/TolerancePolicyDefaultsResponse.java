package com.apriori.acs.models.response.acs.tolerancepolicydefaults;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/TolerancePolicyDefaultsResponse.json")
public class TolerancePolicyDefaultsResponse {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;
}
