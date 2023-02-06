package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/CostResultsProcess.json")
public class CostResultsProcessResponse {
    private String processGroupName;
    private String processName;
    private String index;
    private String displayName;
    private String artifactTypeName;
    private String depth;
}
