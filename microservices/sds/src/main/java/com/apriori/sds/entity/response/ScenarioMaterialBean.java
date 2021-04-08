package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioMaterialBean.json")
@Data
public class ScenarioMaterialBean extends JacksonUtil {
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
