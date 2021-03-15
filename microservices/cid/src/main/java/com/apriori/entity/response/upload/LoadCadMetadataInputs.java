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
        freeBodiesIgnoreMissingComponents = false;
    }

    public String getFileMetadataIdentity() { return fileMetadataIdentity; }

    public LoadCadMetadataInputs setFileMetadataIdentity(String fileMetadataIdentity) {
        this.fileMetadataIdentity = fileMetadataIdentity;
        return this;
    }

    public boolean getKeepFreeBodies() { return keepFreeBodies; }

    public void setKeepFreeBodies(boolean keepFreeBodies) {
        this.keepFreeBodies = keepFreeBodies;
    }

    public boolean getFreeBodiesPreserveCad() { return freeBodiesPreserveCad; }

    public void setFreeBodiesPreserveCad(boolean freeBodiesPreserveCad) {
        this.freeBodiesPreserveCad = freeBodiesPreserveCad;
    }

    public boolean getFreeBodiesIgnoreMissingComponents() { return freeBodiesIgnoreMissingComponents; }

    public void setFreeBodiesIgnoreMissingComponents(boolean freeBodiesIgnoreMissingComponents) {
        this.freeBodiesIgnoreMissingComponents = freeBodiesIgnoreMissingComponents;
    }

    public String getRequestedBy() { return requestedBy; }

    public LoadCadMetadataInputs setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }
}
