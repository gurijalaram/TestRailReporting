package com.apriori.edc.models.response.line.items;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "LineItemsItemsResponse.json")
@Data
@JsonRootName("response")
public class LineItemsItemsResponse extends Pagination {
    private List<LineItemsResponse> items;
}
