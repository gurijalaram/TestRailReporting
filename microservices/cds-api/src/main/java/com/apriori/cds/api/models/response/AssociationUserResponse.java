package com.apriori.cds.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "AssociationUsersSchema.json")
@JsonRootName("response")
@Data
public class AssociationUserResponse extends Pagination {
    private List<AssociationUserItems> items;
}