package com.apriori.acs.api.models.response.acs.activedimensionsbyscenarioiterationkey;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/ActiveDimensionsByScenarioIterationKeyResponse.json")
public class ActiveDimensionsResponse {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;
}
