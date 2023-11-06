package com.apriori.cis.api.models.response.bidpackage;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersPostResponseSchema.json")
public class BidPackageProjectUsersPostResponse {
    private BidPackageProjectUsersPostParameters projectUsers;
}
