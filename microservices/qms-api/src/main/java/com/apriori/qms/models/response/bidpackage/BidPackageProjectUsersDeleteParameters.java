package com.apriori.qms.models.response.bidpackage;

import lombok.Data;

import java.util.List;

@Data
public class BidPackageProjectUsersDeleteParameters {
    private List<BidPackageProjectUsersDeleteResult> failures;
    private List<BidPackageProjectUsersDeleteResult> successes;
}