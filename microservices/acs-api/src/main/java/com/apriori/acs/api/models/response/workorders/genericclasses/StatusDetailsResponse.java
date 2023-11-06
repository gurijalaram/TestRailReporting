package com.apriori.acs.api.models.response.workorders.genericclasses;

public class StatusDetailsResponse {
    private String failureMessage;
    private String statusMessage;

    public String getFailureMessage() {
        return failureMessage;
    }

    public StatusDetailsResponse setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
        return this;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public StatusDetailsResponse setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }
}
