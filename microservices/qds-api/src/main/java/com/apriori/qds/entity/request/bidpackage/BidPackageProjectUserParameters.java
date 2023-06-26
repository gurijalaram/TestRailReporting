package com.apriori.qds.entity.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidPackageProjectUserParameters {
    private String userEmail;
    private String role;
}
