package com.apriori.qds.models.response.bidpackage;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "QdsBidPackageProjectsResponseSchema.json")
public class BidPackageProjectsResponse extends Pagination {
    private List<BidPackageProjectResponse> items;
}
