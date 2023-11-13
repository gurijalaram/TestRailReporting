package com.apriori.acs.api.models.response.acs.costresults;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/CostResultsRoot.json")
public class CostResultsRootItem {
    private ProcessInstanceKey processInstanceKey;
    private ArtifactKey artifactKey;
    private ArrayList<GcdItem> gcds;
    private ResultMapBean resultMapBean;
    private Boolean costingFailed;
    private Boolean secondaryProcess;
    private String depth;
    private String machineName;
    private String vpeName;
}