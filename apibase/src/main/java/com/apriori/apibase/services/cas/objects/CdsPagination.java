package com.apriori.apibase.services.cas.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "PaginationSchema.json")
public class CdsPagination {
    @JsonProperty
    private Boolean isFirstPage;
    @JsonProperty
    private Boolean isLastPage;
    @JsonProperty
    private Integer pageNumber;
    @JsonProperty
    private Integer pageSize;
    @JsonProperty
    private Integer pageItemCount;
    @JsonProperty
    private Integer totalItemCount;
    @JsonProperty
    private Integer totalPageCount;
    @JsonProperty
    private Boolean hasNextPage;
    @JsonProperty
    private Boolean hasPreviousPage;
    @JsonProperty
    private List<Customer> items;

    public CdsPagination setIsFirstPage(Boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
        return this;
    }

    public Boolean getIsFirstPage() {
        return this.isFirstPage;
    }

    public CdsPagination setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
        return this;
    }

    public Boolean getIsLastPage() {
        return this.isLastPage;
    }

    public CdsPagination setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public CdsPagination setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public CdsPagination setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;
    }

    public Integer getPageItemCount() {
        return this.pageItemCount;
    }

    public CdsPagination setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public Integer getTotalItemCount() {
        return this.totalItemCount;
    }

    public CdsPagination setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return this.totalPageCount;
    }

    public CdsPagination setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasNextPage() {
        return this.hasNextPage;
    }

    public CdsPagination setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }

    public Boolean getHasPreviousPage() {
        return this.hasPreviousPage;
    }


    public CdsPagination setItems(List<Customer> items) {
        this.items = items;
        return this;
    }

    public List<Customer> getItems() {
        return this.items;
    }
}
