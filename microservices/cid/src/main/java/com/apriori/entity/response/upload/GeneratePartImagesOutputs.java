package com.apriori.entity.response.upload;

public class GeneratePartImagesOutputs {

    private String cadMetadataIdentity;
    private String webImageIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;

    public String getWebImageIdentity() {
        return webImageIdentity;
    }

    public GeneratePartImagesOutputs setWebImageIdentity(String webImageIdentity) {
        this.webImageIdentity = webImageIdentity;
        return this;
    }

    public String getThumbnailImageIdentity() {
        return thumbnailImageIdentity;
    }

    public GeneratePartImagesOutputs setThumbnailImageIdentity(String thumbnailImageIdentity) {
        this.thumbnailImageIdentity = thumbnailImageIdentity;
        return this;
    }

    public String getDesktopImageIdentity() {
        return desktopImageIdentity;
    }

    public GeneratePartImagesOutputs setDesktopImageIdentity(String desktopImageIdentity) {
        this.desktopImageIdentity = desktopImageIdentity;
        return this;
    }

    public String getCadMetadataIdentity() {
        return cadMetadataIdentity;
    }

    public GeneratePartImagesOutputs setCadMetadataIdentity(String cadMetadataIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
        return this;
    }
}
