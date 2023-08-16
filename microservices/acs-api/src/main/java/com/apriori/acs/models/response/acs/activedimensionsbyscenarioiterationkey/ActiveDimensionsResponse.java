package com.apriori.acs.models.response.acs.activedimensionsbyscenarioiterationkey;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/ActiveDimensionsByScenarioIterationKeyResponse.json")
public class ActiveDimensionsResponse {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;
}
