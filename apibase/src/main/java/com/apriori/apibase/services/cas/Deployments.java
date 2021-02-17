package com.apriori.apibase.services.cas;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/DeploymentsSchema.json")
public class Deployments extends Pagination {
    private List<Deployment> items;
    private Deployments response;

    public Deployments getResponse() {
        return response;
    }

    public Deployments setResponse(Deployments response) {
        this.response = response;
        return this;
    }

    public List<Deployment> getItems() {
        return items;
    }

    public Deployments setItems(List<Deployment> items) {
        this.items = items;
        return this;
    }
}
