package com.apriori.bcs.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "UserPreferencesResponseSchema.json")
public class UserPreferences {
    private List<UserPreference> userPreferences;
}