package com.apriori.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ConfigurationsSchema.json")
public class Configurations extends Pagination {
    private List<Configuration> items;
    private Configurations response;

    public Configurations getResponse() {
        return this.response;
    }

    public List<Configuration> getItems() {
        return this.items;
    }
}