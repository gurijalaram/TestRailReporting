package com.apriori.edcapi.entity.response.line.items;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LineItemsPart {

    private Boolean isSaved;
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
