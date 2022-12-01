package com.apriori.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "AccessAuthorizationsSchema.json")
@JsonRootName("response")
@Data
public class AccessAuthorizations extends Pagination {
    private List<AccessAuthorization> items;
}