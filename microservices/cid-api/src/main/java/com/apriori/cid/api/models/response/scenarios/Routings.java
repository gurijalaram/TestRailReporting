package com.apriori.cid.api.models.response.scenarios;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "RoutingsResponse.json")
@JsonRootName("response")
public class Routings extends Pagination {
    private List<Routing> items;
}
