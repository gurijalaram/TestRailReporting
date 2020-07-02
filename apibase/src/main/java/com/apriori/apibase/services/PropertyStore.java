package com.apriori.apibase.services;

public class PropertyStore {
    private String partIdentity;
    private String batchIdentity;
    private String emailIdentity;

    public String getEmailIdentity() {
        return emailIdentity;
    }

    public PropertyStore setEmailIdentity(String emailIdentity) {
        this.emailIdentity = emailIdentity;
        return this;
    }

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
