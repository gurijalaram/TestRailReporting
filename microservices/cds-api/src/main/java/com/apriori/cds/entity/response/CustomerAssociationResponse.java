package com.apriori.cds.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/CustomerAssociationsSchema.json")
public class CustomerAssociationResponse extends Pagination {
    private List<CustomerAssociationItems> items;
    private CustomerAssociationResponse response;

    public CustomerAssociationResponse getResponse() {
        return this.response;
    }

    public CustomerAssociationResponse setResponse(CustomerAssociationResponse response) {
        this.response = response;
        return this;
    }

    public CustomerAssociationResponse setItems(List<CustomerAssociationItems> items) {
        this.items = items;
        return this;
    }

    public List<CustomerAssociationItems> getItems() {
        return this.items;
    }
}
