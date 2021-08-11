package com.apriori.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "DeploymentsSchema.json")
public class Deployments extends Pagination {
    private List<Deployment> items;
    private Deployments response;

    public Deployments getResponse() {
        return response;
    }

    public List<Deployment> getItems() {
        return items;
    }
}
