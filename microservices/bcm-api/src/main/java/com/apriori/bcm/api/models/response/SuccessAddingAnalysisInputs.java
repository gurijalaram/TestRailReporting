package com.apriori.bcm.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
@Schema(location = "SuccessAddingAnalysisInputs.json")
public class SuccessAddingAnalysisInputs {
    private List<SuccessAnalysisInputs> successes;
    private List<FailureAnalysisInputs> failures = null;
}