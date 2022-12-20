package com.apriori.acs.entity.response.acs.allmaterialstocksinfo;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/AllMaterialStocksInfoResponse.json")
public class AllMaterialStocksInfoResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
