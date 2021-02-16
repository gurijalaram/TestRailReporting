package com.apriori.apibase.services.cid.objects.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "FileOrderResponseSchema.json")
public class FileOrderResponse {
    private String id;
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
