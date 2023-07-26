package com.apriori.cds.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "IDPSchema.json")
@JsonRootName("response")
@Data
public class IdentityProviderPagination extends Pagination {
    private List<IdentityProviderResponse> items;
}
