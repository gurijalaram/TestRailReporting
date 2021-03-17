package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/BatchItemsPostSchema.json")
public class BatchItemsPost {
    private List<Object> batchItems = null;

    public List<Object> getBatchItems() {
        return batchItems;
    }

    public BatchItemsPost setBatchItems(List<Object> batchItems) {
        this.batchItems = batchItems;
        return this;
    }
}