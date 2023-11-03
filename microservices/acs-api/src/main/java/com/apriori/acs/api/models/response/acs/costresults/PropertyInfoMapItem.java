package com.apriori.acs.api.models.response.acs.costresults;

import lombok.Data;

@Data
public class PropertyInfoMapItem {
    private String name;
    private String unitTypeName;
    private String supportedSerializedType;
    private String displayName;
    private String category;
}
