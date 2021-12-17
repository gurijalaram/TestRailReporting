package com.apriori.edcapi.entity.response.line.items;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "LineItemsItemsResponse.json")
@Data
@JsonRootName("response")
public class LineItemsItemsResponse extends Pagination {
    private List<LineItemsResponse> items;
}
