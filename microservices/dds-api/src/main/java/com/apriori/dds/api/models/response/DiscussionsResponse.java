package com.apriori.dds.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "DiscussionsResponseSchema.json")
public class DiscussionsResponse extends Pagination {
    private List<DiscussionResponse> items;
}