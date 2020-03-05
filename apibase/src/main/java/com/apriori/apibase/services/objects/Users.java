package com.apriori.apibase.services.objects;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "CdsUsersSchema.json")
public class Users {


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
    private List<User> items;

    @JsonProperty
    private Users response;

    public Users getResponse() {
        return this.response;
    }

    public Users setResponse(Users response) {
        this.response = response;
        return this;
    }

    public Users setIsFirstPage(Boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
        return this;
    }

    public Boolean getIsFirstPage() {
        return this.isFirstPage;
    }

    public Users setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
        return this;

    }

    public Boolean getIsLastPage() {
        return this.isLastPage;

    }

    public Users setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;

    }

    public Integer getPageNumber() {
        return this.pageNumber;

    }

    public Users setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;

    }

    public Integer getPageSize() {
        return this.pageSize;

    }

    public Users setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;

    }

    public Integer getPageItemCount() {
        return this.pageItemCount;

    }

    public Users setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;

    }

    public Integer getTotalItemCount() {
        return this.totalItemCount;

    }

    public Users setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return this.totalPageCount;

    }

    public Users setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;

    }

    public Boolean getHasNextPage() {
        return this.hasNextPage;

    }

    public Users setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;

    }

    public Boolean getHasPreviousPage() {
        return this.hasPreviousPage;
    }

    public Users setItems(List<User> items) {
        this.items = items;
        return this;
    }

    public List<User> getItems() {
        return this.items;
    }


}

