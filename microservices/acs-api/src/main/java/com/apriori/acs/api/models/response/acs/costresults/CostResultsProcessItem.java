package com.apriori.acs.api.models.response.acs.costresults;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "acs/CostResultsProcess.json")
public class CostResultsProcessItem extends ArrayList<CostResultsRootItem> {
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
