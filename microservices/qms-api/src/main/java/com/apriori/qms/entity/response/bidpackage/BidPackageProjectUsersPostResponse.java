package com.apriori.qms.entity.response.bidpackage;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersPostResponseSchema.json")
public class BidPackageProjectUsersPostResponse {
    private BidPackageProjectUsersPostParameters projectUsers;
}
