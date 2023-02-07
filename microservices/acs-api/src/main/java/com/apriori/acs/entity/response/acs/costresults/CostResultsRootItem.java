package com.apriori.acs.entity.response.acs.costresults;

import lombok.Data;

import java.util.List;

@Data
public class CostResultsRootItem {
    private ProcessInstanceKey processInstanceKey;
    private ArtifactKey artifactKey;
    private List<GCDsItem> gcds;
    private ResultMapBean resultMapBean;
    private Boolean costingFailed;
    private Boolean secondaryProcess;
    private String depth;
}
