package com.apriori.qds.entity.response.bidpackage;

import com.apriori.qds.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectsResponseSchema.json")
public class BidPackageProjectsResponse extends Pagination {
    private List<BidPackageProjectResponse> items;
}
