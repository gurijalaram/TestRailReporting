package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioMaterialBean.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
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
