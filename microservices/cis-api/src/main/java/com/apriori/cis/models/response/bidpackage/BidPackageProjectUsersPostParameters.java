package com.apriori.cis.models.response.bidpackage;

import lombok.Data;

import java.util.List;

@Data
public class BidPackageProjectUsersPostParameters {
    private List<BidPackageProjectUserResponse> successes;
    private List<BidPackageProjectUsersFailureResult> failures;
}
