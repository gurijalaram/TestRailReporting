package com.apriori.cis.models.response.bidpackage;

import lombok.Data;

@Data
public class BidPackageProjectUsersFailureResult {
    private String error;
    private String userEmail;
}
