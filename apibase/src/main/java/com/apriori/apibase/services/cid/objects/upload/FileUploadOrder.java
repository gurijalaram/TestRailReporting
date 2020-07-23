package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUploadOrder {

    @JsonProperty
    private String scenarioName;

    @JsonProperty
    private String fileKey;

    @JsonProperty
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
