package com.apriori.acs.models.response.acs.allmaterialstocksinfo;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/AllMaterialStocksInfoResponse.json")
public class AllMaterialStocksInfoResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
