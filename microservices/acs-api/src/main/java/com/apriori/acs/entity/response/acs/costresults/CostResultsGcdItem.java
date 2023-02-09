package com.apriori.acs.entity.response.acs.costresults;

import lombok.Data;

@Data
public class CostResultsGcdItem {
    private ProcessInstanceKey processInstanceKey;
    private ArtifactKey artifactKey;
    private ResultMapBean resultMapBean;
    private Boolean costingFailed;
    private Boolean secondaryProcess;
    private String depth;
}
