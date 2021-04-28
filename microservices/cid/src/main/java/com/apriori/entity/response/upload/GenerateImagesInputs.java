package com.apriori.entity.response.upload;

public class GenerateImagesInputs {

    private String cadMetadataIdentity;
    private String requestedBy;

    public String getCadMetadataIdentity() {
        return cadMetadataIdentity;
    }

    public GenerateImagesInputs setCadMetadataIdentity(String cadMetadataIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
        return this;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public GenerateImagesInputs setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }
}
