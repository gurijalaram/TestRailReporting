package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioSecondaryProcesses.json")
public class ScenarioSecondaryProcess extends LombokUtil {
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
