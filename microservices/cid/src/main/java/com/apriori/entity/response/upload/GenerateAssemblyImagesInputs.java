package com.apriori.entity.response.upload;

import java.util.List;

public class GenerateAssemblyImagesInputs {

    private String componentIdentity;
    private String scenarioIdentity;
    private String cadMetadataIdentity;
    private String requestedBy;
    private List<GenerateAssemblyImagesInputs> subComponents;

    public String getComponentIdentity() {
        return componentIdentity;
    }

    public GenerateAssemblyImagesInputs setComponentIdentity(String componentIdentity) {
        this.componentIdentity = componentIdentity;
        return this;
    }

    public String getScenarioIdentity() {
        return scenarioIdentity;
    }

    public GenerateAssemblyImagesInputs setScenarioIdentity(String scenarioIdentity) {
        this.scenarioIdentity = scenarioIdentity;
        return this;
    }

    public String getCadMetadataIdentity() {
        return cadMetadataIdentity;
    }

    public GenerateAssemblyImagesInputs setCadMetadataIdentity(String cadMetadataIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
        return this;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public GenerateAssemblyImagesInputs setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }

    public List<GenerateAssemblyImagesInputs> getSubComponents() {
        return subComponents;
    }

    public GenerateAssemblyImagesInputs setSubComponents(List<GenerateAssemblyImagesInputs> subComponents) {
        this.subComponents = subComponents;
        return this;
    }
}
