package com.apriori.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonRootName("response")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "common/PaginationSchema.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer pageItemCount;
    private Integer totalItemCount;
    private Integer totalPageCount;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private Boolean isRestrictedByAccessControl;
}