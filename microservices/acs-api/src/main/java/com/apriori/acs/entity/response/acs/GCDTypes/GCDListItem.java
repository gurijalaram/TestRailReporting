package com.apriori.acs.entity.response.acs.GCDTypes;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GCDTypes.json")
public class GCDListItem {
    public String displayName;
    public String name;
    public String storageType;
    public boolean editable;
}
