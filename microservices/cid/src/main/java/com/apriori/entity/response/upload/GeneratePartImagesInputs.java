package com.apriori.entity.response.upload;

public class GeneratePartImagesInputs {

    private String cadMetadataIdentity;
    private String requestedBy;

    public String getCadMetadataIdentity() {
        return cadMetadataIdentity;
    }

    public GeneratePartImagesInputs setCadMetadataIdentity(String cadMetadataIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
        return this;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public GeneratePartImagesInputs setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }
}
