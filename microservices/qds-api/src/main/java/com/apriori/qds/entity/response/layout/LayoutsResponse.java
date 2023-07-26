package com.apriori.qds.entity.response.layout;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "LayoutsResponseSchema.json")
public class LayoutsResponse extends Pagination {
    private List<LayoutResponse> items;
}
