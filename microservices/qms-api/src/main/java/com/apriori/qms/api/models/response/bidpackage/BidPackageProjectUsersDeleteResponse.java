package com.apriori.qms.api.models.response.bidpackage;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersDeleteResponseSchema.json")
public class BidPackageProjectUsersDeleteResponse {
    private BidPackageProjectUsersDeleteParameters projectUsers;
}
