package com.apriori.edc.api.models.response.line.items;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Schema(location = "LineItemsPart.json")
@Data
public class LineItemParts {
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
    private List<Attributes> attributes;
}
