package com.apriori.apibase.services;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;


@Schema(location = "common/PaginationSchema.json")
@Data
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
