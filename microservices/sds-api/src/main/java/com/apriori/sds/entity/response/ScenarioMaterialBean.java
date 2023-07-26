package com.apriori.sds.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioMaterialBean.json")
@Data
@JsonRootName("response")
public class ScenarioMaterialBean {
    private Boolean isCadMaterialNameValid;
    private String utilizationMode;
    private Boolean initialized;
    private String materialMode;
    private Boolean isUserMaterialNameValid;
    private String vpeDefaultMaterialName;
    private String stockMode;
    private Double userUtilizationOverride;
    private String userMaterialName;
}
