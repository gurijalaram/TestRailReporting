package com.apriori.entity.response.upload;

import java.util.ArrayList;

public class GenerateAssemblyImagesOutputs {

    private String cadMetadataIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
    private String productStructureIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;
    private ArrayList<String> generatedWebImages;

    public String getCadMetadataIdentity() {
        return cadMetadataIdentity;
    }

    public GenerateAssemblyImagesOutputs setCadMetadataIdentity(String cadMetadataIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
        return this;
    }

    public String getComponentIdentity() {
        return componentIdentity;
    }

    public GenerateAssemblyImagesOutputs setComponentIdentity(String componentIdentity) {
        this.componentIdentity = componentIdentity;
        return this;
    }

    public String getScenarioIdentity() {
        return scenarioIdentity;
    }

    public GenerateAssemblyImagesOutputs setScenarioIdentity(String scenarioIdentity) {
        this.scenarioIdentity = scenarioIdentity;
        return this;
    }

    public String getProductStructureIdentity() {
        return productStructureIdentity;
    }

    public GenerateAssemblyImagesOutputs setProductStructureIdentity(String productStructureIdentity) {
        this.productStructureIdentity = productStructureIdentity;
        return this;
    }

    public String getThumbnailImageIdentity() {
        return thumbnailImageIdentity;
    }

    public GenerateAssemblyImagesOutputs setThumbnailImageIdentity(String thumbnailImageIdentity) {
        this.thumbnailImageIdentity = thumbnailImageIdentity;
        return this;
    }

    public String getDesktopImageIdentity() {
        return desktopImageIdentity;
    }

    public GenerateAssemblyImagesOutputs setDesktopImageIdentity(String desktopImageIdentity) {
        this.desktopImageIdentity = desktopImageIdentity;
        return this;
    }

    public ArrayList<String> getGeneratedWebImages() {
        return generatedWebImages;
    }

    public GenerateAssemblyImagesOutputs setGeneratedWebImages(ArrayList<String> generatedWebImages) {
        this.generatedWebImages = generatedWebImages;
        return this;
    }
}
