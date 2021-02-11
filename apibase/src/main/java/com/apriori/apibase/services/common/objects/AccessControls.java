package com.apriori.apibase.services.common.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "common/CdsAccessControlsSchema.json")
public class AccessControls extends Pagination {
    @JsonProperty
    private AccessControls response;
    @JsonProperty
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
