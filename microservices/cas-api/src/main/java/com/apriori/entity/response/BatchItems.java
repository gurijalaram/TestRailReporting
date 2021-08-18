package com.apriori.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "BatchItemsSchema.json")
public class BatchItems extends Pagination {
    private BatchItems response;
    private List<BatchItem> items;

    public BatchItems getResponse() {
        return response;
    }

    public List<BatchItem> getItems() {
        return items;
    }
}