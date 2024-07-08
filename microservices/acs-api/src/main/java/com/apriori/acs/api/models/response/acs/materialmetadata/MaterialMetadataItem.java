package com.apriori.acs.api.models.response.acs.materialmetadata;

import lombok.Data;

@Data
public class MaterialMetadataItem {
    private String displayName;
    private String name;
    private String type;
    private String unitTypeName;
}
