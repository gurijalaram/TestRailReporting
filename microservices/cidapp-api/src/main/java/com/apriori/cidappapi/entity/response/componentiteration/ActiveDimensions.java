package com.apriori.cidappapi.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActiveDimensions {
    private PropertyValueMap propertyValueMap;
    private PropertyInfoMap propertyInfoMap;

    public PropertyValueMap getPropertyValueMap() {
        return propertyValueMap;
    }

    public void setPropertyValueMap(PropertyValueMap propertyValueMap) {
        this.propertyValueMap = propertyValueMap;
    }

    public PropertyInfoMap getPropertyInfoMap() {
        return propertyInfoMap;
    }

    public void setPropertyInfoMap(PropertyInfoMap propertyInfoMap) {
        this.propertyInfoMap = propertyInfoMap;
    }
}
