package com.apriori.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "SitesSchema.json")
public class Sites extends Pagination {
    private List<Site> items;
    private Sites response;

    public Sites getResponse() {
        return this.response;
    }

    public List<Site> getItems() {
        return this.items;
    }
}