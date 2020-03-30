package com.apriori.apibase.services.objects;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "CdsCustomersSchema.json")
public class Customers extends Customer {
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

    @JsonProperty
    private Customers response;

    @JsonProperty
    private String salesforceId;

    public String getSalesforceId() {
        return this.salesforceId;
    }

    public Customers setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
        return this;
    }

    public Customers getResponse() {
        return this.response;
    }

    public Customers setResponse(Customers response) {
        this.response = response;
        return this;
    }

    public Customers setIsFirstPage(Boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
        return this;
    }

    public Boolean getIsFirstPage() {
        return this.isFirstPage;
    }

    public Customers setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
        return this;

    }

    public Boolean getIsLastPage() {
        return this.isLastPage;

    }

    public Customers setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;

    }

    public Integer getPageNumber() {
        return this.pageNumber;

    }

    public Customers setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;

    }

    public Integer getPageSize() {
        return this.pageSize;

    }

    public Customers setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;

    }

    public Integer getPageItemCount() {
        return this.pageItemCount;

    }

    public Customers setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;

    }

    public Integer getTotalItemCount() {
        return this.totalItemCount;

    }

    public Customers setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return this.totalPageCount;

    }

    public Customers setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;

    }

    public Boolean getHasNextPage() {
        return this.hasNextPage;

    }

    public Customers setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;

    }

    public Boolean getHasPreviousPage() {
        return this.hasPreviousPage;
    }

    public Customers setItems(List<Customer> items) {
        this.items = items;
        return this;
    }

    public List<Customer> getItems() {
        return this.items;
    }


}
