package com.apriori.acs.entity.response.acs.materialsinfo;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/MaterialsInfoStockMachiningResponse.json")
public class MaterialsInfoStockMachiningResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
