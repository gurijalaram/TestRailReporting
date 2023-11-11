package com.apriori.bcm.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

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
}
