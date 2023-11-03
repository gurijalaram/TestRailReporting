package com.apriori.cis.api.models.response.bidpackage;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemResponseSchema.json")
public class BidPackageProjectItemResponse {
    private String identity;
    private BidPackageItem bidPackageItem;
}
