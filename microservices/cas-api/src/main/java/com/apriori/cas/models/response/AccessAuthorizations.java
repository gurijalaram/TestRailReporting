package com.apriori.cas.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "AccessAuthorizationsSchema.json")
@JsonRootName("response")
@Data
public class AccessAuthorizations extends Pagination {
    private List<AccessAuthorization> items;
}