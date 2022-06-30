package com.apriori.apibase.services.cas;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "IdentityProvidersSchema.json")
@JsonRootName("response")
@Data
public class IdentityProviders extends Pagination {
    private List<IdentityProvider> items;
    private IdentityProviders response;
}