package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileItemsEntity {

    @JsonProperty
    private String identity;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    private String createdAt;

    @JsonProperty
    private String customerIdentity;

    @JsonProperty
    private String deploymentIdentity;

    @JsonProperty
    private String installationIdentity;

    @JsonProperty
    private String applicationIdentity;

    @JsonProperty
    private String userIdentity;

    @JsonProperty
    private String filename;

    @JsonProperty
    private String folder;

    @JsonProperty("filesize")
    private String fileSize;

    @JsonProperty
    private String md5hash;

    public String getIdentity() {
        return identity;
    }

    public FileItemsEntity setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public FileItemsEntity setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public FileItemsEntity setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public FileItemsEntity setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public FileItemsEntity setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return installationIdentity;
    }

    public FileItemsEntity setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public FileItemsEntity setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public FileItemsEntity setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public FileItemsEntity setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getFolder() {
        return folder;
    }

    public FileItemsEntity setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public String getFileSize() {
        return fileSize;
    }

    public FileItemsEntity setFileSize(String fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getMd5hash() {
        return md5hash;
    }

    public FileItemsEntity setMd5hash(String md5hash) {
        this.md5hash = md5hash;
        return this;
    }
}
