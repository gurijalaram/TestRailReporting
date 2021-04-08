package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioAvailableProcessGroupSelection.json")
@Data
public class ScenarioAvailableProcessGroupSelection extends JacksonUtil {
    private String manuallyCosted;
    private String displayName;
    private String pgName;
}
