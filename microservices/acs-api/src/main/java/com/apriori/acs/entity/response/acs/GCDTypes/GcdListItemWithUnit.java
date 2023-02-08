package com.apriori.acs.entity.response.acs.GCDTypes;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GCDTypes.json")
public class GcdListItemWithUnit {
    private String displayName;
    private String name;
    private String storageType;
    private String unitType;
    private Boolean editable;
}
