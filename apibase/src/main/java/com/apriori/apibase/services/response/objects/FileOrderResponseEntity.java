package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "FileOrderResponseSchema.json")
public class FileOrderResponseEntity {

    @JsonProperty
    private String id;

    @JsonProperty
    private Boolean resourceCreated;

    public String getId() {
        return id;
    }

    public FileOrderResponseEntity setId(String id) {
        this.id = id;
        return this;
    }

    public Boolean getResourceCreated() {
        return resourceCreated;
    }

    public FileOrderResponseEntity setResourceCreated(Boolean resourceCreated) {
        this.resourceCreated = resourceCreated;
        return this;
    }
}
