package com.apriori.acs.api.models.response.acs.allmaterialstocksinfo;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/AllMaterialStocksInfoResponse.json")
public class AllMaterialStocksInfoResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
