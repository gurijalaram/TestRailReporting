package com.apriori.qms.entity.response.bidpackage;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "BidPackageProjectUsersPostResponseSchema.json")
public class BidPackageProjectUsersPostResponse extends ArrayList<BidPackageProjectUserResponse> {

}
