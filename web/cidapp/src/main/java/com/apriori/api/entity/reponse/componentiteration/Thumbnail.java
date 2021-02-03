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

    public Thumbnail setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Thumbnail setImage(String image) {
        this.image = image;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public Thumbnail setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

}
