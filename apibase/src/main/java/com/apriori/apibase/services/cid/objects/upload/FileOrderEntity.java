package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileOrderEntity {

    @JsonProperty
    private String scenarioName;

    @JsonProperty
    private String fileKey;

    @JsonProperty
    private String fileName;

    public String getScenarioName() {
        return scenarioName;
    }

    public FileOrderEntity setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getFileKey() {
        return fileKey;
    }

    public FileOrderEntity setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileOrderEntity setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
