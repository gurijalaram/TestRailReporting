package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

import java.util.List;

@Schema(location = "BillOfMaterialsSchema.json")
public class BillOfMaterialsWrapper extends CommonResponse {

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

    @JsonProperty("items")
    private List<BillOfMaterial> billOfMaterialsList;


    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public BillOfMaterialsWrapper setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public BillOfMaterialsWrapper setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public BillOfMaterialsWrapper setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public BillOfMaterialsWrapper setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageItemCount() {
        return pageItemCount;
    }

    public BillOfMaterialsWrapper setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public BillOfMaterialsWrapper setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public BillOfMaterialsWrapper setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public BillOfMaterialsWrapper setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public BillOfMaterialsWrapper setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }

    public List<BillOfMaterial> getBillOfMaterialsList() {
        return billOfMaterialsList;
    }

    public BillOfMaterialsWrapper setBillOfMaterialsList(List<BillOfMaterial> billOfMaterialsList) {
        this.billOfMaterialsList = billOfMaterialsList;
        return this;
    }
}
