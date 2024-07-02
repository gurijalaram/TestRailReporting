package com.apriori.acs.api.models.response.acs.materialmetadata;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/MaterialMetadataResponse.json")
public class MaterialMetadataResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
