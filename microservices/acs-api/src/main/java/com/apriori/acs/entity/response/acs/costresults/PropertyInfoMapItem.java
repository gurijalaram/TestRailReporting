package com.apriori.acs.entity.response.acs.costresults;

import lombok.Data;

@Data
public class PropertyInfoMapItem {
    private String name;
    private String unitTypeName;
    private String supportedSerializedType;
    private String displayName;
}
