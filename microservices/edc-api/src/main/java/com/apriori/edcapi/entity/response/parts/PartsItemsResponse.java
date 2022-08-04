package com.apriori.edcapi.entity.response.parts;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "PartsItemResponse.json")
@Data
@JsonRootName("response")

public class PartsItemsResponse extends Pagination {
    private List<PartsResponse> items;
}
