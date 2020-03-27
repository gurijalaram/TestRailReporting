package com.apriori.apibase.services.fms.objects;

import com.apriori.apibase.http.enums.Schema;

import java.util.List;

@Schema(location = "fms/FmsFilesSchema.json")
public class FilesResponse {
    private Boolean isFirstPage;

    private Boolean isLastPage;

    private Integer pageNumber;

    private Integer pageSize;

    private Integer pageItemCount;

    private Integer totalItemCount;

    private Integer totalPageCount;

    private Boolean hasNextPage;

    private Boolean hasPreviousPage;

    private List<FileResponse> items;

    private FilesResponse response;

    public FilesResponse getResponse() {
        return this.response;
    }

    public FilesResponse setResponse(FilesResponse response) {
        this.response = response;
        return this;
    }

    public Boolean getIsFirstPage() {
        return this.isFirstPage;
    }

    public FilesResponse setIsFirstPage(Boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
        return this;
    }

    public Boolean getIsLastPage() {
        return this.isLastPage;
    }

    public FilesResponse setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
        return this;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public FilesResponse setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;

    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public FilesResponse setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageItemCount() {
        return this.pageItemCount;
    }

    public FilesResponse setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;

    }

    public Integer getTotalItemCount() {
        return this.totalItemCount;
    }

    public FilesResponse setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return this.totalPageCount;

    }

    public FilesResponse setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Boolean getHasNextPage() {
        return this.hasNextPage;
    }

    public FilesResponse setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasPreviousPage() {
        return this.hasPreviousPage;
    }

    public FilesResponse setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }

    public List<FileResponse> getItems() {
        return this.items;
    }

    public FilesResponse setItems(List<FileResponse> items) {
        this.items = items;
        return this;
    }
}
