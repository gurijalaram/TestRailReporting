package com.apriori.acs.api.models.response.workorders.upload;

import com.apriori.acs.api.models.response.FileItemsEntity;

import lombok.Data;

import java.util.List;

@Data
public class FileUpload {
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer pageItemCount;
    private Integer totalItemCount;
    private Integer totalPageCount;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private List<FileItemsEntity> items;
}
