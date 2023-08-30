package com.apriori.qa.ach.ui.enums;

public enum CustomerDeploymentsEnum {
    PRODUCTION("Production"),
    SANDBOX("Sandbox"),
    PREVIEW("Preview");

    private final String name;

    CustomerDeploymentsEnum(String name) {
        this.name = name;
    }

    public String getDeploymentName() {
        return name;
    }
}
