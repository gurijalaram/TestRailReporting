package com.apriori.cds.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "cds/DeploymentsSchema.json")
public class Deployments extends Pagination {
    @JsonProperty
    private List<Deployment> items;

    @JsonProperty
    private Deployments response;

    public Deployments getResponse() {
        return response;
    }

    public void setResponse(Deployments response) {
        this.response = response;
    }

    public List<Deployment> getItems() {
        return items;
    }

    public Deployments setItems(List<Deployment> items) {
        this.items = items;
        return this;
    }
}
