package com.apriori.cds.api.enums;

public enum DeploymentEnum {
    PRODUCTION("Production"),
    SANDBOX("Sandbox");

    private final String deployment;

    DeploymentEnum(String deployment) {
        this.deployment = deployment;
    }

    public String getDeployment() {
        return deployment;
    }
}
