package com.apriori.edcapi.entity.response.parts;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "PostPart.json")
@Data
@JsonRootName("response")
public class PostPartResponse {
    private Boolean isSaved;
    private Boolean isUserPart;
    private String identity;
    private String lineItemIdentity;
    private String manufacturerPartNumber;
    private String classification;
    private String description;
    private Double averageCost;
    private Double minimumCost;
    private String mountType;
    private Integer pinCount;
    private String availability;
    private String dataSheetUrl;
    private String rohs;
    private String rohsVersion;
    private String type;
    private String status;
}
