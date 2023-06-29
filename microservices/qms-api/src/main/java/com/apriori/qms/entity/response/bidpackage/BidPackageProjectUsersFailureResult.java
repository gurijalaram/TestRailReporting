package com.apriori.qms.entity.response.bidpackage;

import lombok.Data;

@Data
public class BidPackageProjectUsersFailureResult {
    private String error;
    private String userEmail;
}
