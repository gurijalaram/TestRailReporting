package com.apriori.cis.api.models.response.userpreferences;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ExtendedUserPreferencesResponseSchema.json")
@Data
@JsonRootName("response")
public class ExtendedUserPreferencesResponse extends Pagination {
    private List<ExtendedUserPreferenceParameters> items;
}
