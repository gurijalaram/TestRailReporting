package com.apriori.cis.api.models.response.bidpackage;

import lombok.Data;

import java.util.List;

@Data
public class BidPackageProjectUsersPostParameters {
    private List<BidPackageProjectUserResponse> successes;
    private List<BidPackageProjectUsersFailureResult> failures;
}
