package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioSecondaryProcesses.json")
@Data
public class ScenarioSecondaryProcess extends JacksonUtil {
    private String name;
    private String displayName;
    private String plantName;
    private String processGroupName;
    private ScenarioSecondaryProcess children;
    private Boolean optional;
    private Boolean included;
    private Boolean overriden;
    private Boolean alternNode;
    private Integer includedChildrenCount;
    private Boolean leafNode;

}
