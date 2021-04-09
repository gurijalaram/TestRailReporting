package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(location = "sds/ScenarioAvailableProcessGroupSelection.json")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioAvailableProcessGroupSelection extends JacksonUtil {
    private String manuallyCosted;
    private String displayName;
    private String pgName;
}
