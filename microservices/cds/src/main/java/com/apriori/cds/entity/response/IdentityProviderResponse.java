
package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/IDPSchema.json")
public class IdentityProviderResponse {

    private IdentityProviderResponse response;

    public IdentityProviderResponse getResponse() {
        return this.response;
    }

    public IdentityProviderResponse setResponse(IdentityProviderResponse response) {
        this.response = response;
        return this;
    }
}
