package com.apriori.acs.models.response.acs.materialsinfo;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/MaterialsInfoPlasticMoldingResponse.json")
public class MaterialsInfoPlasticMoldingResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
