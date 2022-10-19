package com.apriori.acs.entity.response.acs.tolerancepolicydefaults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/TolerancePolicyDefaultsResponse.json")
public class TolerancePolicyDefaultsResponse {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;
}
