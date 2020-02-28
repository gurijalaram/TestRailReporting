package com.apriori.apibase.services.objects;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "CdsResponseSchema.json")
public class Response {

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
