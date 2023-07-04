package com.apriori.cisapi.entity.response.bidpackage;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersPostResponseSchema.json")
public class BidPackageProjectUsersPostResponse {
    private BidPackageProjectUsersPostParameters projectUsers;
}
