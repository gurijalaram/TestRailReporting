package com.apriori.ach.api.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Failure {
    private UserPreferenceInFailureResponse userPreference;
    private String errorMessage;
}