package com.apriori.apibase.services.common.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/IdentityProvidersSchema.json")
public class IdentityProviders extends Pagination {
    private List<IdentityProvider> items;
    private IdentityProviders response;

    public IdentityProviders getResponse() {
        return this.response;
    }

    public IdentityProviders setResponse(IdentityProviders response) {
        this.response = response;
        return this;
    }

    public IdentityProviders setItems(List<IdentityProvider> items) {
        this.items = items;
        return this;
    }

    public List<IdentityProvider> getItems() {
        return this.items;
    }
}