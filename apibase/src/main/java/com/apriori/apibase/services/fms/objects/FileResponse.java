package com.apriori.apibase.services.fms.objects;

import com.apriori.apibase.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "fms/FmsFileSchema.json")
public class FileResponse {
    @JsonProperty
    private String identity;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

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

    @JsonProperty
    private Long filesize;

    @JsonProperty
    private String md5hash;

    @JsonProperty
    private FileResponse response;

    public FileResponse getResponse() {
        return this.response;
    }

    public FileResponse setResponse(FileResponse response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public FileResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public FileResponse setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public FileResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCustomerIdentity() {
        return this.customerIdentity;
    }

    public FileResponse setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return this.deploymentIdentity;
    }

    public FileResponse setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return this.installationIdentity;
    }

    public FileResponse setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getApplicationIdentity() {
        return this.applicationIdentity;
    }

    public FileResponse setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getUserIdentity() {
        return this.userIdentity;
    }

    public FileResponse setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getFilename() {
        return this.filename;
    }

    public FileResponse setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getFolder() {
        return this.folder;
    }

    public FileResponse setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public Long getFilesize() {
        return this.filesize;
    }

    public FileResponse setFilesize(Long filesize) {
        this.filesize = filesize;
        return this;
    }

    public String getMd5hash() {
        return this.md5hash;
    }

    public FileResponse setMd5hash(String md5hash) {
        this.md5hash = md5hash;
        return this;
    }
}
