package com.apriori.vds.models.response.configuration;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ConfigurationsItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class ConfigurationsItems extends Pagination {
    private List<Configuration> items;
}
