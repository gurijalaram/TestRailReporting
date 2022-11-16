package com.apriori.qms.entity.response.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPackageItem {
    private String identity;
    private String bidPackageIdentity;
    private String projectIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
    private String iterationIdentity;
}
