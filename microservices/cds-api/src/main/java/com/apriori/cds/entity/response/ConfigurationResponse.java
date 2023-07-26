package com.apriori.cds.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ConfigurationsSchema.json")
@JsonRootName("response")
@Data
public class ConfigurationResponse extends Pagination {
    private List<ConfigurationItems> items;
}
