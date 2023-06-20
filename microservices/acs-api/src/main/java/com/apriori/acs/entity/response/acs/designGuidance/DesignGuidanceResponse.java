package com.apriori.acs.entity.response.acs.designGuidance;

import com.apriori.acs.entity.response.acs.materialsinfo.PropertyInfoMap;
import com.apriori.acs.entity.response.acs.materialsinfo.PropertyValuesList;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/DesignGuidanceResponse.json")
public class DesignGuidanceResponse {
    private List<PropertyValuesList> propertyValuesList;
    private PropertyInfoMap propertyInfoMap;
}
