package com.apriori.acs.api.models.response.acs.costresults;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostResultsGcdItem {
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
