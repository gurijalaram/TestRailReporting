package com.apriori.dfs.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ProcessGroupsSchema.json")
@Data
public class ProcessGroups extends Pagination {

    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Boolean restrictedByAccessControl;
    private List<ProcessGroup> items;
}