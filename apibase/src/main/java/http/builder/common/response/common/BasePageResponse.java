package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasePageResponse extends BaseResponse {
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

    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public BasePageResponse setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public BasePageResponse setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public BasePageResponse setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public BasePageResponse setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageItemCount() {
        return pageItemCount;
    }

    public BasePageResponse setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public BasePageResponse setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public BasePageResponse setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public BasePageResponse setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public BasePageResponse setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }
}
