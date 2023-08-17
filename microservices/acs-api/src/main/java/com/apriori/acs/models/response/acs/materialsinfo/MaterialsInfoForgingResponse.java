package com.apriori.acs.models.response.acs.materialsinfo;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/MaterialsInfoForgingResponse.json")
public class MaterialsInfoForgingResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
