package com.apriori.acs.models.response.workorders.upload;

public class FileUploadOrder {
    private String scenarioName;
    private String fileKey;
    private String fileName;

    public String getScenarioName() {
        return scenarioName;
    }

    public FileUploadOrder setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getFileKey() {
        return fileKey;
    }

    public FileUploadOrder setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileUploadOrder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
