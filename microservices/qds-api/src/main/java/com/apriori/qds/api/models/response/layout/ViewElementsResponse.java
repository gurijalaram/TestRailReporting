package com.apriori.qds.api.models.response.layout;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ViewElementsResponseSchema.json")
public class ViewElementsResponse extends Pagination {
    private List<ViewElementResponse> items;
}
