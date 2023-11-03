package com.apriori.cis.api.models.response.bidpackage;

import lombok.Data;

@Data
public class BidPackageProjectUsersFailureResult {
    private String error;
    private String userEmail;
}
