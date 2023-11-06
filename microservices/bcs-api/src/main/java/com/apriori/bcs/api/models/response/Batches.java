package com.apriori.bcs.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "CustomerBatchesItemsResponseSchema.json")
public class Batches extends Pagination {
    private List<Batch> items;
}
