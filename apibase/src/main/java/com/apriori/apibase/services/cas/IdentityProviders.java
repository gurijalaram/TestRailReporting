package com.apriori.apibase.services.cas;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "IdentityProvidersSchema.json")
public class IdentityProviders extends Pagination {
    private List<IdentityProvider> items;
    private IdentityProviders response;

    public IdentityProviders getResponse() {
        return this.response;
    }

    public List<IdentityProvider> getItems() {
        return this.items;
    }
}