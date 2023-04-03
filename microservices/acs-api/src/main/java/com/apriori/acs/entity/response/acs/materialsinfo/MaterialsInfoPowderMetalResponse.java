package com.apriori.acs.entity.response.acs.materialsinfo;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/MaterialsInfoPowderMetalResponse.json")
public class MaterialsInfoPowderMetalResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
