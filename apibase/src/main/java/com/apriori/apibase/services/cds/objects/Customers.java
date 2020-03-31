package com.apriori.apibase.services.cds.objects;

import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "CdsCustomersSchema.json")
public class Customers extends Customer {
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer pageItemCount;
    private Integer totalItemCount;
    private Integer totalPageCount;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private List<Customer> items;
    private Customers response;
    private String salesforceId;

    public Boolean getFirstPage() {
        return this.isFirstPage;
    }

    public Customers setFirstPage(Boolean firstPage) {
        this.isFirstPage = firstPage;
        return this;
    }

    public Boolean getLastPage() {
        return this.isLastPage;
    }

    public Customers setLastPage(Boolean lastPage) {
        this.isLastPage = lastPage;
        return this;
    }

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
