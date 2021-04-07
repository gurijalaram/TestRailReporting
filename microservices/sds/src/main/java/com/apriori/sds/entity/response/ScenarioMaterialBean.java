package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioMaterialBean.json")
public class ScenarioMaterialBean extends LombokUtil {
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
