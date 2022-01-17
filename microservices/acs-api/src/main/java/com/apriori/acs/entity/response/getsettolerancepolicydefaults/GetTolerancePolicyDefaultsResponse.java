package com.apriori.acs.entity.response.getsettolerancepolicydefaults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "GetTolerancePolicyDefaultsResponse.json")
public class GetTolerancePolicyDefaultsResponse {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;
}
