package com.apriori.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomerBatchesSchema.json")
@Data
public class CustomerBatches extends Pagination {
    private List<CustomerBatch> items;
    private CustomerBatches response;
}
