package com.apriori.qms.models.response.bidpackage;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemsResponseSchema.json")
public class BidPackageProjectItemsResponse extends Pagination {
    List<BidPackageProjectItemResponse> items;
}