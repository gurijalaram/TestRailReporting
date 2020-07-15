package com.apriori.apibase.services.cid.objects.cost;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "FileOrderResponseSchema.json")
public class FileOrderResponse {

    @JsonProperty
    private String id;

    @JsonProperty
    private Boolean resourceCreated;

    public String getId() {
        return id;
    }

    public FileOrderResponse setId(String id) {
        this.id = id;
        return this;
    }

    public Boolean getResourceCreated() {
        return resourceCreated;
    }

    public FileOrderResponse setResourceCreated(Boolean resourceCreated) {
        this.resourceCreated = resourceCreated;
        return this;
    }
}
