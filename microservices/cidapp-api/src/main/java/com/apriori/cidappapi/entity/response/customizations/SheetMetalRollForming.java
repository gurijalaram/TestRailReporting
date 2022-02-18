package com.apriori.cidappapi.entity.response.customizations;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SheetMetalRollForming {
    private String processGroupIdentity;
    private String defaultMaterialName;
    private String defaultMaterialIdentity;
}