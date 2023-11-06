package com.apriori.acs.api.models.response.acs.productioninfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialBean {
    private Boolean initialized;
    private String vpeDefaultMaterialName;
    private String materialMode;
    private Boolean isUserMaterialNameValid;
    private Boolean isCadMaterialNameValid;
    private Integer userUtilizationOverride;
    private String stockMode;
}
