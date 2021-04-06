package com.apriori.entity.response.upload;

public class GeneratePartImagesOutputs {

    private String cadMetadataIdentity;
    private String webImageIdentity;
    private String thumbnailImageIdentity;
    private String desktopImageIdentity;

    public GeneratePartImagesOutputs GeneratePartImagesOutputs(String cadMetadataIdentity, String webImageIdentity, String thumbnailImageIdentity, String desktopImageIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
        this.webImageIdentity = webImageIdentity;
        this.thumbnailImageIdentity = thumbnailImageIdentity;
        this.desktopImageIdentity = desktopImageIdentity;
        return this;
    }

    public String getWebImageIdentity() {
        return webImageIdentity;
    }

    public void setWebImageIdentity(String webImageIdentity) {
        this.webImageIdentity = webImageIdentity;
    }

    public String getThumbnailImageIdentity() {
        return thumbnailImageIdentity;
    }

    public void setThumbnailImageIdentity(String thumbnailImageIdentity) {
        this.thumbnailImageIdentity = thumbnailImageIdentity;
    }

    public String getDesktopImageIdentity() {
        return desktopImageIdentity;
    }

    public void setDesktopImageIdentity(String desktopImageIdentity) {
        this.desktopImageIdentity = desktopImageIdentity;
    }

    public String getCadMetadataIdentity() {
        return cadMetadataIdentity;
    }

    public void setCadMetadataIdentity(String cadMetadataIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
    }
}
