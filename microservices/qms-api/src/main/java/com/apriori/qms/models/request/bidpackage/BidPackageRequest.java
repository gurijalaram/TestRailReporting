package com.apriori.qms.models.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPackageRequest {
    BidPackageParameters bidPackage;
}
