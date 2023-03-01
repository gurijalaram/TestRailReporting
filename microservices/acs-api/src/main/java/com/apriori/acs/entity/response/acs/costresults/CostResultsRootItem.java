package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "acs/CostResultsRoot.json")
public class CostResultsRootItem extends ArrayList {
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
