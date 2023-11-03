package com.apriori.acs.api.models.request.workorders.cost.productioninfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductionInfoMaterial {
    private Boolean initialized;
    private String vpeDefaultMaterialName;
    private String materialMode;
    private Boolean isUserMaterialNameValid;
    private Boolean isCadMaterialNameValid;
    private Integer userUtilizationOverride;
}