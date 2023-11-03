package com.apriori.sds.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "FeatureDecisionsSchema.json")
@Data
@JsonRootName("response")
public class FeatureDecisionsResponse {
    Boolean isRestrictedByAccessControl;
    Boolean isLastPage;
    Boolean isFirstPage;
    Integer pageNumber;
    Integer pageSize;
    Integer pageItemCount;
    Integer totalItemCount;
    Integer totalPageCount;
    Boolean hasNextPage;
    Boolean hasPreviousPage;
    List<Items> items;
}
