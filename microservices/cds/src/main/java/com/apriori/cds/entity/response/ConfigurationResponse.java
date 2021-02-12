package com.apriori.cds.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "cds/ConfigurationsSchema.json")
public class ConfigurationResponse extends Pagination {
    @JsonProperty
    private List<ConfigurationItems> items;
    @JsonProperty
    private ConfigurationResponse response;

    public ConfigurationResponse getResponse() {
        return this.response;
    }

    public ConfigurationResponse setResponse(ConfigurationResponse response) {
        this.response = response;
        return this;
    }

    public List<ConfigurationItems> getItems() {
        return this.items;
    }

    public ConfigurationResponse setItems(List<ConfigurationItems> items) {
        this.items = items;
        return this;
    }
}
