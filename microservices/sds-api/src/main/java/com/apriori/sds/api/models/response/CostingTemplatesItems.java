package com.apriori.sds.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CostingTemplatesItems.json")
@Data
@JsonRootName("response")
public class CostingTemplatesItems extends Pagination {
    private List<CostingTemplate> items;
}
