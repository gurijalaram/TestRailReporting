package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CostingTemplatesItems.json")
@Data
@JsonRootName("response")
public class CostingTemplatesItems extends Pagination {
    private List<CostingTemplate> items;
}
