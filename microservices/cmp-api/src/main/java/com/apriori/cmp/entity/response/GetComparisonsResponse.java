package com.apriori.cmp.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "GetComparisonResponse.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName("response")
public class GetComparisonsResponse extends Pagination {
    List<GetComparisonResponse> items;
}
