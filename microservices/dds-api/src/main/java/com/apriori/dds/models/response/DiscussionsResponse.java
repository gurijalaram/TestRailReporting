package com.apriori.dds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "DiscussionsResponseSchema.json")
public class DiscussionsResponse extends Pagination {
    private List<DiscussionResponse> items;
}
