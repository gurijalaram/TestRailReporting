package com.apriori.qms.models.response.layout;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "LayoutConfigurationsResponseSchema.json")
public class LayoutConfigurationsResponse extends Pagination {
    private List<LayoutConfigurationResponse> items;
}
