package com.apriori.ach.entity.request;

import com.apriori.ach.entity.response.UserPreference;

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