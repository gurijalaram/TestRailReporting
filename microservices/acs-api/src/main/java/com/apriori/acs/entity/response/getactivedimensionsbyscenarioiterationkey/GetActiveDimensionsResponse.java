package com.apriori.acs.entity.response.getactivedimensionsbyscenarioiterationkey;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "GetActiveDimensionsByScenarioIterationKeyResponse.json")
public class GetActiveDimensionsResponse {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;
}
