package com.apriori.entity.response.upload;

public class FileUploadInputs {
    private String scenarioName;
    private String fileKey;
    private String fileName;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;

    public FileUploadInputs() {
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

    public boolean getKeepFreeBodies() {
        return keepFreeBodies;
    }

    public FileUploadInputs setKeepFreeBodies(boolean keepFreeBodies) {
        this.keepFreeBodies = keepFreeBodies;
        return this;
    }

    public boolean getFreeBodiesPreserveCad() {
        return freeBodiesPreserveCad;
    }

    public FileUploadInputs setFreeBodiesPreserveCad(boolean freeBodiesPreserveCad) {
        this.freeBodiesPreserveCad = freeBodiesPreserveCad;
        return this;
    }

    public boolean getFreeBodiesIgnoreMissingComponents() {
        return freeBodiesIgnoreMissingComponents;
    }

    public FileUploadInputs setFreeBodiesIgnoreMissingComponents(boolean freeBodiesIgnoreMissingComponents) {
        this.freeBodiesIgnoreMissingComponents = freeBodiesIgnoreMissingComponents;
        return this;
    }

}
