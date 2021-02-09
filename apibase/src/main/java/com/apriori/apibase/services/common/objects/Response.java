package com.apriori.apibase.services.common.objects;

import com.apriori.utils.http.enums.Schema;

import java.util.List;

// TODO: 09/02/2021 ciene - might not need this class
@Schema(location = "cds/CdsResponseSchema.json")
public class Response {
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer pageItemCount;
    private Integer totalItemCount;
    private Integer totalPageCount;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private List<Object> items;

    public Response setIsFirstPage(Boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
        return this;
    }

    public Boolean getIsFirstPage() {
        return this.isFirstPage;
    }

    public Response setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
        return this;

    }

    public Boolean getIsLastPage() {
        return this.isLastPage;

    }

    public Response setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;

    }

    public Integer getPageNumber() {
        return this.pageNumber;

    }

    public Response setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;

    }

    public Integer getPageSize() {
        return this.pageSize;

    }

    public Response setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;

    }

    public Integer getPageItemCount() {
        return this.pageItemCount;

    }

    public Response setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;

    }

    public Integer getTotalItemCount() {
        return this.totalItemCount;

    }

    public Response setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return this.totalPageCount;

    }

    public Response setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;

    }

    public Boolean getHasNextPage() {
        return this.hasNextPage;

    }

    public Response setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;

    }

    public Boolean getHasPreviousPage() {
        return this.hasPreviousPage;
    }

    public Response setItems(List items) {
        this.items = items;
        return this;
    }

    public List<Object> getItems() {
        return this.items;
    }


}
