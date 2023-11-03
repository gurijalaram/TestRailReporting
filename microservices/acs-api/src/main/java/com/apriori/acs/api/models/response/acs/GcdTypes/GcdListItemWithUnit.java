package com.apriori.acs.api.models.response.acs.GcdTypes;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GcdTypes.json")
public class GcdListItemWithUnit {
    private String displayName;
    private String name;
    private String storageType;
    private String unitType;
    private Boolean editable;
}
