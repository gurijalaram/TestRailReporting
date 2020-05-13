package com.apriori.apibase.services.cis.objects.requests;

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
