package com.apriori.cidappapi.entity.response.scenarios;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "RoutingsResponse.json")
@JsonRootName("response")
public class Routings extends Pagination {
    private List<Routing> items;
}
