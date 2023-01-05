package com.apriori.qms.entity.response.layout;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "LayoutConfigurationsResponseSchema.json")
public class LayoutConfigurationsResponse extends Pagination {
    private List<LayoutConfigurationResponse> items;
}
