package com.apriori.cidappapi.models.response.scenarios;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "RoutingsResponse.json")
@JsonRootName("response")
public class Routings extends Pagination {
    private List<Routing> items;
}
