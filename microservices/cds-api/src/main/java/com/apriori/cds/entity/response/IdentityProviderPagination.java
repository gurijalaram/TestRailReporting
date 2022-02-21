
package com.apriori.cds.entity.response;

import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

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
