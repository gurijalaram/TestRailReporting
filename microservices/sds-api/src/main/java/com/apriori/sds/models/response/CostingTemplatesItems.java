package com.apriori.sds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CostingTemplatesItems.json")
@Data
@JsonRootName("response")
public class CostingTemplatesItems extends Pagination {
    private List<CostingTemplate> items;
}
