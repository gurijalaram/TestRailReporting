package com.apriori.cus.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
@Schema(location = "FailureAddingAnalysisInputs.json")
public class FailureAddingAnalysisInputs {
    private List<SuccessAnalysisInputs> successes = null;
    private List<FailureAnalysisInputs> failures;
}