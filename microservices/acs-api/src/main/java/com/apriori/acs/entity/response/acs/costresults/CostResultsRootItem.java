package com.apriori.acs.entity.response.acs.costresults;

import lombok.Data;

import java.util.List;

@Data
public class CostResultsRootItem {
    private ProcessInstanceKey processInstanceKey;
    private ArtifactKey artifactKey;
    private List<GcdItem> gcds;
    private ResultMapBean resultMapBean;
    private Boolean costingFailed;
    private Boolean secondaryProcess;
    private String depth;
    private String machineName;
    private String vpeName;
}
