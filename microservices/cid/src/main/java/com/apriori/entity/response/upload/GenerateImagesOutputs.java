package com.apriori.entity.response.upload;

public class GenerateImagesOutputs {

    private String cadMetadataIdentity;
    private String webImageIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;

    public GenerateImagesOutputs GeneratePartImagesOutputs(String cadMetadataIdentity, String webImageIdentity,
                                                           String thumbnailImageIdentity,
                                                           String desktopImageIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
        this.webImageIdentity = webImageIdentity;
        this.thumbnailImageIdentity = thumbnailImageIdentity;
        this.desktopImageIdentity = desktopImageIdentity;
        return this;
    }

    public String getWebImageIdentity() {
        return webImageIdentity;
    }

    public GenerateImagesOutputs setWebImageIdentity(String webImageIdentity) {
        this.webImageIdentity = webImageIdentity;
        return this;
    }

    public String getThumbnailImageIdentity() {
        return thumbnailImageIdentity;
    }

    public GenerateImagesOutputs setThumbnailImageIdentity(String thumbnailImageIdentity) {
        this.thumbnailImageIdentity = thumbnailImageIdentity;
        return this;
    }

    public String getDesktopImageIdentity() {
        return desktopImageIdentity;
    }

    public GenerateImagesOutputs setDesktopImageIdentity(String desktopImageIdentity) {
        this.desktopImageIdentity = desktopImageIdentity;
        return this;
    }

    public String getCadMetadataIdentity() {
        return cadMetadataIdentity;
    }

    public GenerateImagesOutputs setCadMetadataIdentity(String cadMetadataIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
        return this;
    }
}
