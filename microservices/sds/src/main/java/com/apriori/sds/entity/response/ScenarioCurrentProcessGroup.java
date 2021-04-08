package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioCurrentProcessGroup.json")
@Data
public class ScenarioCurrentProcessGroup extends JacksonUtil {
    private String first;
    private String second;
}
