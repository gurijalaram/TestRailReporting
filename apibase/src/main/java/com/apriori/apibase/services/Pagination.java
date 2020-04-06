package com.apriori.apibase.services;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "PaginationSchema.json")
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

    public Pagination setIsFirstPage(Boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
        return this;
    }

    public Boolean getIsFirstPage() {
        return this.isFirstPage;
    }

    public Pagination setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
        return this;
    }

    public Boolean getIsLastPage() {
        return this.isLastPage;
    }

    public Pagination setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public Pagination setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Pagination setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;
    }

    public Integer getPageItemCount() {
        return this.pageItemCount;
    }

    public Pagination setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public Integer getTotalItemCount() {
        return this.totalItemCount;
    }

    public Pagination setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return this.totalPageCount;
    }

    public Pagination setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasNextPage() {
        return this.hasNextPage;
    }

    public Pagination setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }

    public Boolean getHasPreviousPage() {
        return this.hasPreviousPage;
    }
}
