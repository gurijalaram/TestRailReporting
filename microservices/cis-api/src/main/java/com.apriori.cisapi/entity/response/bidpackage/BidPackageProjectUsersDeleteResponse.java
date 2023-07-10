package com.apriori.cisapi.entity.response.bidpackage;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersDeleteResponseSchema.json")
public class BidPackageProjectUsersDeleteResponse {
    private BidPackageProjectUsersDeleteParameters projectUsers;
}