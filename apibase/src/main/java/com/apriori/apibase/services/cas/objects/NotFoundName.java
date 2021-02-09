package com.apriori.apibase.services.cas.objects;

import com.apriori.utils.http.enums.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "cas/NotFoundName.json")
public class NotFoundName {
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
    private List<Object> items = null;

    public Boolean getIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(Boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public Boolean getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageItemCount() {
        return pageItemCount;
    }

    public void setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
    }


    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }


    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
}
