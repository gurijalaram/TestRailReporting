package com.apriori.apibase.services.nts.objects;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "NtsGetEmailResponseSchema.json")
public class Email {
    private Email response;
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String customerIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String applicationIdentity;
    private String senderAddress;
    private String recipientAddress;
    private String subject;
    private String content;
    private Boolean sendAsBatch;
    private Object[] attachments;
    private String status;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public Email setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public Email getResponse() {
        return response;
    }

    public Email setResponse(Email response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public Email setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Email setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Email setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Email setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public Email setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public Email setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return installationIdentity;
    }

    public Email setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public Email setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public Email setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
        return this;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public Email setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Email setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Email setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getSendAsBatch() {
        return sendAsBatch;
    }

    public Email setSendAsBatch(Boolean sendAsBatch) {
        this.sendAsBatch = sendAsBatch;
        return this;
    }

    public Object[] getAttachments() {
        return attachments;
    }

    public Email setAttachments(Object[] attachments) {
        this.attachments = attachments;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Email setStatus(String status) {
        this.status = status;
        return this;
    }


}
