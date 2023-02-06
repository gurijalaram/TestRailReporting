package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(location = "acs/CostResultsProcess.json")
@NoArgsConstructor
public class CostResultsRootResponse {
    private ProcessInstanceKey processInstanceKey;
    private ArtifactKey artifactKey;
    private List<GCDsItem> gcds;
    private String resultMapBean;
    private Boolean costingFailed;
    private Boolean secondaryProcess;
    private String depth;
}
