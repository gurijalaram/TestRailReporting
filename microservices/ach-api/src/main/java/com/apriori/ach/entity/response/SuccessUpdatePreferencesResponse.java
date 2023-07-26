package com.apriori.ach.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
@Schema(location = "SuccessUpdatePreferencesResponseSchema.json")
public class SuccessUpdatePreferencesResponse {
    private List<Success> successes;
    private List<Failure> failures = null;
}