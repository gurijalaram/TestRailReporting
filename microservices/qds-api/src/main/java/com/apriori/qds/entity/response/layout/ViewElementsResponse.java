package com.apriori.qds.entity.response.layout;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ViewElementsResponseSchema.json")
public class ViewElementsResponse extends Pagination {
    private List<ViewElementResponse> items;
}
