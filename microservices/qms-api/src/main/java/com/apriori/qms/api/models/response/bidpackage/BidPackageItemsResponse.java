package com.apriori.qms.api.models.response.bidpackage;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageItemsResponseSchema.json")
public class BidPackageItemsResponse extends Pagination {
    private List<BidPackageItemResponse> items;
}