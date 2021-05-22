package com.apriori.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartNestingDiagram {
    private String identity;
    private String image;
    private String imageType;

    public String getIdentity() {
        return identity;
    }

    public PartNestingDiagram setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getImage() {
        return image;
    }

    public PartNestingDiagram setImage(String image) {
        this.image = image;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public PartNestingDiagram setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }
}
