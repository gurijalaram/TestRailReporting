package com.apriori.dfs.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "DigitalFactoriesSchema.json")
@Data
public class DigitalFactories extends Pagination {
    //cn - i have queried to see if these 3 fields should be there because they look like duplicates
    private Boolean firstPage;
    private Boolean lastPage;
    private Boolean restrictedByAccessControl;
    private List<DigitalFactory> items;
}