package com.apriori.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

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
