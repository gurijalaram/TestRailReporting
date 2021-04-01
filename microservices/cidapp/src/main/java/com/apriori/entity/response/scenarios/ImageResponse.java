package com.apriori.entity.response.scenarios;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cidapp/ImageResponseSchema.json")
public class ImageResponse {
    private ImageResponse response;
    private String identity;
    private String image;
    private String imageType;

    public ImageResponse getResponse() {
        return response;
    }

    public ImageResponse setResponse(ImageResponse response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public ImageResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ImageResponse setImage(String image) {
        this.image = image;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public ImageResponse setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }
}
