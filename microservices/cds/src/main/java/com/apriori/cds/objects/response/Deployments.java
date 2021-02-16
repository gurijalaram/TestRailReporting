package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "cds/DeploymentsSchema.json")
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
