
package com.apriori.cds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/IDPSchema.json")
public class IdentityProviderPagination extends Pagination {
    private IdentityProviderPagination response;
    private List<IdentityProviderResponse> items;

    public IdentityProviderPagination getResponse() {
        return this.response;
    }

    public IdentityProviderPagination setResponse(IdentityProviderPagination response) {
        this.response = response;
        return this;
    }

    public List<IdentityProviderResponse> getItems() {
        return this.items;
    }
}
