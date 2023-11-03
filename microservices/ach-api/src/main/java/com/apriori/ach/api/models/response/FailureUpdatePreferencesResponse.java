package com.apriori.ach.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
@Schema(location = "FailureUpdatePreferencesResponseSchema.json")
public class FailureUpdatePreferencesResponse {
    private List<Success> successes = null;
    private List<Failure> failures;
}