package com.apriori.acs.entity.response.acs.costresults;

import lombok.Data;

import java.util.List;

@Data
public class CostResultsProcessItem {
    private ProcessInstanceKey processInstanceKey;
    private ArtifactKey artifactKey;
    private List<GcdItem> gcds;
    private String machineName;
    private String vpeName;
    private ResultMapBean resultMapBean;
    private Boolean costingFailed;
    private Boolean secondaryProcess;
    private String depth;
}
