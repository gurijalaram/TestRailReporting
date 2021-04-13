package com.apriori.entity.response.upload;

public class FileUploadInputs {
    private String scenarioName;
    private String fileKey;
    private String fileName;

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
}
