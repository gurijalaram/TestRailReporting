package com.apriori.cis.models.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPackageItemParameters {
    private String componentIdentity;
    private String scenarioIdentity;
    private String iterationIdentity;
}
