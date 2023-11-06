package com.apriori.bcs.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "PartsItemsResponseSchema.json")
public class Parts extends Pagination {
    private List<Part> items;
}
