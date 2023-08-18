package com.apriori.cis.models.response.bidpackage;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersDeleteResponseSchema.json")
public class BidPackageProjectUsersDeleteResponse {
    private BidPackageProjectUsersDeleteParameters projectUsers;
}
