package com.apriori.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/CustomerBatchesSchema.json")
public class CustomerBatches extends Pagination {
    private List<CustomerBatch> items;
    private CustomerBatches response;

    public CustomerBatches getResponse() {
        return this.response;
    }

    public List<CustomerBatch> getItems() {
        return this.items;
    }
}
