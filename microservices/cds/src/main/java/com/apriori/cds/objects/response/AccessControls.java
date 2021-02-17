package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/AccessControlsSchema.json")
public class AccessControls extends Pagination {
    private AccessControls response;
    private List<AccessControl> items;

    public AccessControls getResponse() {
        return response;
    }

    public AccessControls setResponse(AccessControls response) {
        this.response = response;
        return this;
    }

    public AccessControls setItems(List<AccessControl> items) {
        this.items = items;
        return this;
    }

    public List<AccessControl> getItems() {
        return this.items;
    }
}
