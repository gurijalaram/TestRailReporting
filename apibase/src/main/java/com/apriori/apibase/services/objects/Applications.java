package com.apriori.apibase.services.objects;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "CdsApplicationsSchema.json")
public class Applications {
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
    private List<Application> items;
    
    @JsonProperty
    private Applications response;
    
    public Applications getResponse() {
        return this.response;
    }
    
    public Applications setResponse(Applications response) {
        this.response = response;
        return this;
    }
    
    public Applications setIsFirstPage(Boolean isFirstPage) { 
        this.isFirstPage = isFirstPage;
        return this;
    }
    
    public Boolean getIsFirstPage() { 
        return this.isFirstPage;
    }
    
    public Applications setIsLastPage(Boolean isLastPage) { 
        this.isLastPage = isLastPage;
        return this;
        
    }
    
    public Boolean getIsLastPage() { 
        return this.isLastPage;
        
    }
    
    public Applications setPageNumber(Integer pageNumber) { 
        this.pageNumber = pageNumber;
        return this;
        
    }
    
    public Integer getPageNumber() { 
        return this.pageNumber;
        
    }
    
    public Applications setPageSize(Integer pageSize) { 
        this.pageSize = pageSize;
        return this;
        
    }
    
    public Integer getPageSize() { 
        return this.pageSize;
        
    }
    
    public Applications setPageItemCount(Integer pageItemCount) { 
        this.pageItemCount = pageItemCount;
        return this;
        
    }
    
    public Integer getPageItemCount() { 
        return this.pageItemCount;
        
    }
    
    public Applications setTotalItemCount(Integer totalItemCount) { 
        this.totalItemCount = totalItemCount;
        return this;
        
    }
    
    public Integer getTotalItemCount() { 
        return this.totalItemCount;
        
    }
    
    public Applications setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    public Integer getTotalPageCount() { 
        return this.totalPageCount;
        
    }
    
    public Applications setHasNextPage(Boolean hasNextPage) { 
        this.hasNextPage = hasNextPage;
        return this;
        
    }
    
    public Boolean getHasNextPage() { 
        return this.hasNextPage;
        
    }
    
    public Applications setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;

    }
    
    public Boolean getHasPreviousPage() {
        return this.hasPreviousPage;
    }
    
    public Applications setItems(List<Application> items) {
        this.items = items;
        return this;
    }

    public List<Application> getItems() {
        return this.items;
    }
    

}
