package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/AssociationUsersSchema.json")
public class AssociationUserResponse extends Pagination {
    private List<AssociationUserItems> items;
    private AssociationUserResponse response;

    public AssociationUserResponse getResponse() {
        return this.response;
    }

    public AssociationUserResponse setResponse(AssociationUserResponse response) {
        this.response = response;
        return this;
    }

    public AssociationUserResponse setItems(List<AssociationUserItems> items) {
        this.items = items;
        return this;
    }

    public List<AssociationUserItems> getItems() {
        return this.items;
    }
}
