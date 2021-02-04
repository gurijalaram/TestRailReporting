package com.apriori.apibase.services.cds.objects.AccessControls;

import com.apriori.apibase.services.Pagination;
import com.apriori.apibase.services.cds.objects.AccessControls.AccessControlItems;
import com.apriori.apibase.services.cds.objects.Customer;
import com.apriori.apibase.services.cds.objects.Customers;
import com.apriori.utils.http.enums.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Schema(location = "cds/CdsAccessControlsSchema.json")
public class AccessControlResponse extends Pagination {

    private AccessControlResponse response;
    private List<AccessControlItems> items;

    public AccessControlResponse getResponse() {
        return response;
    }

    public AccessControlResponse setResponse(AccessControlResponse response) {
        this.response = response;
        return this;
    }

    public AccessControlResponse setItems(List<AccessControlItems> items) {
        this.items = items;
       return this;
    }

    public List<AccessControlItems> getItems() {
        return this.items;
    }
}
