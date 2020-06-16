package com.apriori.apibase.services;

public class PropertyStore {
    private String partIdentity;
    private String batchIdentity;

    public String getPartIdentity() {
        return partIdentity;
    }

    public PropertyStore setPartIdentity(String partIdentity) {
        this.partIdentity = partIdentity;
        return this;
    }

    public String getBatchIdentity() {
        return batchIdentity;
    }

    public PropertyStore setBatchIdentity(String batchIdentity) {
        this.batchIdentity = batchIdentity;
        return this;
    }
}
