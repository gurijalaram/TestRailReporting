package com.apriori.qds.api.models.request.bidpackage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPackageItemParameters {
    private String componentIdentity;
    private String scenarioIdentity;
    private String iterationIdentity;
}
