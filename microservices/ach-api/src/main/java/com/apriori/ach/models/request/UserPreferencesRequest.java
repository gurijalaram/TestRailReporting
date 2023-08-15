package com.apriori.ach.models.request;

import com.apriori.ach.models.response.UserPreference;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class UserPreferencesRequest {
    private List<UserPreference> userPreferences;
}