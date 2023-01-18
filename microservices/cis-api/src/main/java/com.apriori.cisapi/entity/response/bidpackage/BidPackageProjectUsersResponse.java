package com.apriori.cisapi.entity.response.bidpackage;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectUsersResponseSchema.json")
public class BidPackageProjectUsersResponse extends Pagination {
    private List<BidPackageProjectUserResponse> items;
}
