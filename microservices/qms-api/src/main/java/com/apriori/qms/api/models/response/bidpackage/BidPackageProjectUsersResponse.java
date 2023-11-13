package com.apriori.qms.api.models.response.bidpackage;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersResponseSchema.json")
public class BidPackageProjectUsersResponse extends Pagination {
    List<BidPackageProjectUserResponse> items;
}