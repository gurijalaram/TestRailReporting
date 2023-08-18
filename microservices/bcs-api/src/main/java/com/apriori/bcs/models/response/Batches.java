package com.apriori.bcs.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "CustomerBatchesItemsResponseSchema.json")
public class Batches extends Pagination {
    private List<Batch> items;
}
