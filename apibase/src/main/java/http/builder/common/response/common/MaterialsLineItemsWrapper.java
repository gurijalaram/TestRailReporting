package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

import java.util.List;

@Schema(location = "MaterialsLineItemsWrapperSchema.json")
public class MaterialsLineItemsWrapper extends CommonResponse{

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
    private List<MaterialLineItem> materialLineItems;

    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public MaterialsLineItemsWrapper setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public MaterialsLineItemsWrapper setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public MaterialsLineItemsWrapper setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public MaterialsLineItemsWrapper setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageItemCount() {
        return pageItemCount;
    }

    public MaterialsLineItemsWrapper setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public MaterialsLineItemsWrapper setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public MaterialsLineItemsWrapper setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public MaterialsLineItemsWrapper setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public MaterialsLineItemsWrapper setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }

    public List<MaterialLineItem> getMaterialLineItems() {
        return materialLineItems;
    }

    public MaterialsLineItemsWrapper setMaterialLineItems(List<MaterialLineItem> materialLineItems) {
        this.materialLineItems = materialLineItems;
        return this;
    }
}
