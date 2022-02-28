package com.apriori.acs.entity.response.acs.getsettolerancepolicydefaults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GetTolerancePolicyDefaultsResponse.json")
public class GetTolerancePolicyDefaultsResponse {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;
}
