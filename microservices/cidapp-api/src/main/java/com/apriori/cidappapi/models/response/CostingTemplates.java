package com.apriori.cidappapi.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;
import com.apriori.models.response.component.CostingTemplate;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(location = "CostingTemplateResponse.json")
@JsonRootName("response")
public class CostingTemplates extends Pagination {
    private List<CostingTemplate> items;
}
