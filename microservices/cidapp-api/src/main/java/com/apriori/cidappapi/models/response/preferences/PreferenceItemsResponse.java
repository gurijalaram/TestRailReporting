package com.apriori.cidappapi.models.response.preferences;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "PreferencesItemsResponse.json")
@AllArgsConstructor
@Builder
@Data
@JsonRootName("response")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PreferenceItemsResponse extends Pagination {
    List<PreferenceResponse> items;
    private Boolean isRestrictedByAccessControl;
}
