package com.apriori.edc.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartsRequest {
    private Boolean actionRequired;
    private String availability;
    private Double averageCost;
    private String classification;
    private String dataSheetUrl;
    private String description;
    private String externalId;
    private String identity;
    private Boolean isActionRequired;
    private Boolean isSaved;
    private Boolean isUserPart;
    private String lineItemIdentity;
    private String manufacturer;
    private String manufacturerPartNumber;
    private Double minimumCost;
    private String mountType;
    private Integer pinCount;
    private String rohs;
    private String rohsVersion;
    private Boolean saved;
    private String status;
    private String type;
    private Boolean userPart;
}
