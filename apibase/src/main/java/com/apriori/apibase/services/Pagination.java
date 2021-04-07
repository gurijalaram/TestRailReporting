package com.apriori.apibase.services;

import com.apriori.utils.http.enums.Schema;


@Schema(location = "PaginationSchema.json")
public class Pagination extends LombokUtil {
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer pageItemCount;
    private Integer totalItemCount;
    private Integer totalPageCount;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
}
