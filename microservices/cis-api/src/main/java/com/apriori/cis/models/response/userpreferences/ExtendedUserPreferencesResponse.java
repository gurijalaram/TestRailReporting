package com.apriori.cis.models.response.userpreferences;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ExtendedUserPreferencesResponseSchema.json")
@Data
@JsonRootName("response")
public class ExtendedUserPreferencesResponse extends Pagination {
    private List<ExtendedUserPreferenceParameters> items;
}