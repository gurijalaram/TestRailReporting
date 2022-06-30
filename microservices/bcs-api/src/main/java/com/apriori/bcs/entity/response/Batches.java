package com.apriori.bcs.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "CustomerBatchesItemsResponseSchema.json")
public class Batches extends Pagination {
    private List<Batch> items;
}
