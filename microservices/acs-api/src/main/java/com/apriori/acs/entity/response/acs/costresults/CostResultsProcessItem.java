package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

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
