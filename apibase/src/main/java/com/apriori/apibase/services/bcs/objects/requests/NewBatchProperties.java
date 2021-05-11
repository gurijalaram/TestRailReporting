package com.apriori.apibase.services.bcs.objects.requests;

public class NewBatchProperties {
    private String externalId;

    public String getExternalId() {
        return this.externalId;
    }

    public NewBatchProperties setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }
}
