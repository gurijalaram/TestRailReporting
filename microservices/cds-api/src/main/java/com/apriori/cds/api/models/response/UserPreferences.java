package com.apriori.cds.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "UserPreferencesSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonRootName("response")
public class UserPreferences extends Pagination {
    private List<UserPreference> items;
}