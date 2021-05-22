package com.apriori.nts.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Schema(location = "nts/NtsNotificationSchema.json")
public class Notification {
    private String applicationIdentity;
    private String customerIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private Boolean isUrgent;
    private String type;
    private String title;
    private String content;


    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime displayAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime hideAt;

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public Notification setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public Notification setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public Notification setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return installationIdentity;
    }

    public Notification setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public Notification setUrgent(Boolean urgent) {
        isUrgent = urgent;
        return this;
    }

    public String getType() {
        return type;
    }

    public Notification setType(String type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Notification setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Notification setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getDisplayAt() {
        return displayAt;
    }

    public Notification setDisplayAt(LocalDateTime displayAt) {
        this.displayAt = displayAt;
        return this;
    }

    public LocalDateTime getHideAt() {
        return hideAt;
    }

    public Notification setHideAt(LocalDateTime hideAt) {
        this.hideAt = hideAt;
        return this;
    }
}