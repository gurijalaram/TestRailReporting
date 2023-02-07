package com.apriori.acs.entity.response.acs.costresults;

import lombok.Data;

import java.util.List;

@Data
public class ResultMapBean {
    private PropertyValueMap propertyValueMap;
    private List<PropertyInfoMap> propertyInfoMap;
}
