package com.apriori.cidappapi.models.response.scenarios;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ImageResponseSchema.json")
@Data
public class ImageResponse {
    private ImageResponse response;
    private String identity;
    private String image;
    private String imageType;
}
