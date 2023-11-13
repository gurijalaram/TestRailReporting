package com.apriori.cid.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "GetComponentsResponse.json")
@AllArgsConstructor
@Builder
@Data
@JsonRootName("response")
public class GetComponentResponse extends Pagination {
    private List<GetComponentItems> items;
}