package com.apriori.acs.entity.response.acs.materialsinfo;

import com.apriori.acs.entity.response.acs.allmaterialstocksinfo.PropertyInfoMap;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/MaterialsInfoResponse.json")
public class MaterialsInfoResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
