package com.apriori.apibase.services.fms.objects;

import com.apriori.apibase.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "fms/FmsUploadSchema.json")
public class UploadResponse {
    @JsonProperty
    private String identity;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonProperty
    private String updatedBy;

    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    @JsonProperty
    private String deletedBy;

    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;

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

    public String getIdentity() {
        return this.identity;
    }

    public UploadResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UploadResponse setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public UploadResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public UploadResponse setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public UploadResponse setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public UploadResponse setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public UploadResponse setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getCustomerIdentity() {
        return this.customerIdentity;
    }

    public UploadResponse setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return this.deploymentIdentity;
    }

    public UploadResponse setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return this.installationIdentity;
    }

    public UploadResponse setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getApplicationIdentity() {
        return this.applicationIdentity;
    }

    public UploadResponse setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getUserIdentity() {
        return this.userIdentity;
    }

    public UploadResponse setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getFilename() {
        return this.filename;
    }

    public UploadResponse setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getFolder() {
        return this.folder;
    }

    public UploadResponse setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public Long getFilesize() {
        return this.filesize;
    }

    public UploadResponse setFilesize(Long filesize) {
        this.filesize = filesize;
        return this;
    }

    public String getMd5hash() {
        return this.md5hash;
    }

    public UploadResponse setMd5hash(String md5hash) {
        this.md5hash = md5hash;
        return this;
    }
}
