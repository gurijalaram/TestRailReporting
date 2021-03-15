package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "FileOrderResponseSchema.json")
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
