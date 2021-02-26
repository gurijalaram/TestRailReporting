package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioIterations.json")
public class ScenarioIterations {
    private String createdAt;
    private String customerIdentity;
    private String[] scenarioProcesses;
    private String hasThumbnail;
    private String costingMessage;
    private String createdBy;
    private String identity;
    private String iteration;
    private String hasWebImage;
    private String[] scenarioDtcIssues;
    private String updatedAt;
    private String[] scenarioCustomAttributes;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public String[] getScenarioProcesses() {
        return scenarioProcesses;
    }

    public String getHasThumbnail() {
        return hasThumbnail;
    }

    public String getCostingMessage() {
        return costingMessage;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getIdentity() {
        return identity;
    }

    public String getIteration() {
        return iteration;
    }

    public String getHasWebImage() {
        return hasWebImage;
    }

    public String[] getScenarioDtcIssues() {
        return scenarioDtcIssues;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String[] getScenarioCustomAttributes() {
        return scenarioCustomAttributes;
    }
}
