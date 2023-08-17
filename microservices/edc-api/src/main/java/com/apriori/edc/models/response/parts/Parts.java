package com.apriori.edc.models.response.parts;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "Parts.json")
@Data
@JsonRootName("response")
public class Parts {
    private Boolean isSaved;
    private Boolean isActionRequired;
    private Boolean isUserPart;
    private String identity;
    private String lineItemIdentity;
    private String externalId;
    private String manufacturerPartNumber;
    private String manufacturer;
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
    private String status;
    private String type;
}
