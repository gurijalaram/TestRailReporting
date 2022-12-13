package com.apriori.cusapi.entity.response;

import com.apriori.utils.http.enums.Schema;

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
public class PreferenceItemsResponse {
    List<PreferenceResponse> items;
    private Boolean isRestrictedByAccessControl;
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private int pageNumber;
    private int pageSize;
    private int pageItemCount;
    private int totalItemCount;
    private int totalPageCount;
}
