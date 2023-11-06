package com.apriori.edc.api.models.response.line.items;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "LineItemsItemsResponse.json")
@Data
@JsonRootName("response")
public class LineItemsItemsResponse extends Pagination {
    private List<LineItemsResponse> items;
}
