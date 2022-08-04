package com.apriori.edcapi.entity.response.line.items;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;
@Schema
@Data
public class LineItemsPart {

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
