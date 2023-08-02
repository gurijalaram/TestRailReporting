package com.apriori.cis.models.response.bidpackage;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersResponseSchema.json")
public class BidPackageProjectUsersResponse extends Pagination {
    List<BidPackageProjectUserResponse> items;
}
