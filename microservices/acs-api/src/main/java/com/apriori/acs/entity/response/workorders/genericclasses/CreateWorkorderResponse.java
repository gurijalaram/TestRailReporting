package com.apriori.acs.entity.response.workorders.genericclasses;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "workorders/FileOrderResponseSchema.json")
public class CreateWorkorderResponse {
    private String id;
    private Boolean resourceCreated;

    public String getId() {
        return id;
    }

    public CreateWorkorderResponse setId(String id) {
        this.id = id;
        return this;
    }

    public Boolean getResourceCreated() {
        return resourceCreated;
    }

    public CreateWorkorderResponse setResourceCreated(Boolean resourceCreated) {
        this.resourceCreated = resourceCreated;
        return this;
    }
}
