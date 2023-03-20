package com.apriori.ach.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Failure {
    private UserPreferenceInFailureResponse userPreference;
    private String errorMessage;
}