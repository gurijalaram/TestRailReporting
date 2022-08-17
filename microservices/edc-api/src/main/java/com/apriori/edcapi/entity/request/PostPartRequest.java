package com.apriori.edcapi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPartRequest {
    private String availability;
    private String averageCost;
    private String classification;
    private String dataSheetUrl;
    private String description;
    private Boolean isUserPart;
    private String manufacturer;
    private String manufacturerPartNumber;
    private String minimumCost;
    private String mountType;
    private String pinCount;
    private String rohs;
    private String rohsVersion;
}
