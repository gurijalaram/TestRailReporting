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

    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public Pagination setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public Pagination setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Pagination setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Pagination setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageItemCount() {
        return pageItemCount;
    }

    public Pagination setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public Pagination setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public Pagination setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public Pagination setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public Pagination setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }
}
