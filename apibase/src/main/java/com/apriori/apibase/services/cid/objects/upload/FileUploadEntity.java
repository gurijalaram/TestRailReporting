package com.apriori.apibase.services.cid.objects.upload;

import com.apriori.apibase.services.response.objects.FileItemsEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileUploadEntity {

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
    private List<FileItemsEntity> items;

    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public FileUploadEntity setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public FileUploadEntity setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public FileUploadEntity setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public FileUploadEntity setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageItemCount() {
        return pageItemCount;
    }

    public FileUploadEntity setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public FileUploadEntity setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public FileUploadEntity setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public FileUploadEntity setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public FileUploadEntity setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }

    public Collection<FileItemsEntity> getItems() {
        return items;
    }

    public FileUploadEntity setItems(ArrayList<FileItemsEntity> items) {
        this.items = items;
        return this;
    }
}
