package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Thumbnail {

    @JsonProperty("identity")
    private String identity;
    @JsonProperty("image")
    private String image;
    @JsonProperty("imageType")
    private String imageType;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

}
