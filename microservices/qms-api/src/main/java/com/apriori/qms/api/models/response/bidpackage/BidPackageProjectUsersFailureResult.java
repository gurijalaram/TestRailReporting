package com.apriori.qms.api.models.response.bidpackage;

import lombok.Data;

@Data
public class BidPackageProjectUsersFailureResult {
    private String error;
    private String userEmail;
}
