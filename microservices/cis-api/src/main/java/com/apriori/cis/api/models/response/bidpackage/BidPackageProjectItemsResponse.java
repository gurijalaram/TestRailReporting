package com.apriori.cis.api.models.response.bidpackage;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemsResponseSchema.json")
public class BidPackageProjectItemsResponse {
    private List<BidPackageProjectItemResponse> projectItems;
}
