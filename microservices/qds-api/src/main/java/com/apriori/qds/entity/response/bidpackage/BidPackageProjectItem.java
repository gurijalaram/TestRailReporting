package com.apriori.qds.entity.response.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidPackageProjectItem {
    private String identity;
    private String bidPackageIdentity;
    private String projectIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
    private String iterationIdentity;
}
