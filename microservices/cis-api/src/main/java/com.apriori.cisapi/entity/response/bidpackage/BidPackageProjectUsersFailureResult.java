package com.apriori.cisapi.entity.response.bidpackage;

import lombok.Data;

@Data
public class BidPackageProjectUsersFailureResult {
    private String error;
    private String userEmail;
}
