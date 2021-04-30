package com.apriori.entity.response.upload;

public class FileUploadInputs {
    private String scenarioName;
    private String fileKey;
    private String fileName;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;

    public FileUploadInputs() {
        keepFreeBodies = false;
        keepFreeBodies = true;
        freeBodiesPreserveCad = false;
        freeBodiesIgnoreMissingComponents = true;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public FileUploadInputs setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getFileKey() {
        return fileKey;
    }

    public FileUploadInputs setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileUploadInputs setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public boolean isKeepFreeBodies() {
        return keepFreeBodies;
    }

    public void setKeepFreeBodies(boolean keepFreeBodies) {
        this.keepFreeBodies = keepFreeBodies;
    }

    public boolean isFreeBodiesPreserveCad() {
        return freeBodiesPreserveCad;
    }

    public void setFreeBodiesPreserveCad(boolean freeBodiesPreserveCad) {
        this.freeBodiesPreserveCad = freeBodiesPreserveCad;
    }

    public boolean isFreeBodiesIgnoreMissingComponents() {
        return freeBodiesIgnoreMissingComponents;
    }

    public void setFreeBodiesIgnoreMissingComponents(boolean freeBodiesIgnoreMissingComponents) {
        this.freeBodiesIgnoreMissingComponents = freeBodiesIgnoreMissingComponents;
    }
}
