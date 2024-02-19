package com.apriori.bcm.api.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class FailureAnalysisInputs {
    private String error;
    private String inputRowIdentity;
}