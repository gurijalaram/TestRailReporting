package com.apriori.entity.response.upload;

public class LoadCadMetadataInputs {

    private String fileMetadataIdentity;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
    private String requestedBy;

    public LoadCadMetadataInputs() {
        keepFreeBodies = false;
        freeBodiesPreserveCad = false;
        freeBodiesIgnoreMissingComponents = true;
    }

    public String getFileMetadataIdentity() {
        return fileMetadataIdentity;
    }

    public LoadCadMetadataInputs setFileMetadataIdentity(String fileMetadataIdentity) {
        this.fileMetadataIdentity = fileMetadataIdentity;
        return this;
    }

    public boolean getKeepFreeBodies() {
        return keepFreeBodies;
    }

    public LoadCadMetadataInputs setKeepFreeBodies(boolean keepFreeBodies) {
        this.keepFreeBodies = keepFreeBodies;
        return this;
    }

    public boolean getFreeBodiesPreserveCad() {
        return freeBodiesPreserveCad;
    }

    public LoadCadMetadataInputs setFreeBodiesPreserveCad(boolean freeBodiesPreserveCad) {
        this.freeBodiesPreserveCad = freeBodiesPreserveCad;
        return this;
    }

    public boolean getFreeBodiesIgnoreMissingComponents() {
        return freeBodiesIgnoreMissingComponents;
    }

    public LoadCadMetadataInputs setFreeBodiesIgnoreMissingComponents(boolean freeBodiesIgnoreMissingComponents) {
        this.freeBodiesIgnoreMissingComponents = freeBodiesIgnoreMissingComponents;
        return this;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public LoadCadMetadataInputs setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }
}
